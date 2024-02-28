package com.six.homework.supertrader.controllers;

import com.six.homework.supertrader.AbstractTestClass;
import com.six.homework.supertrader.controllers.order.InvalidOrderException;
import com.six.homework.supertrader.controllers.order.OrderController;
import com.six.homework.supertrader.controllers.trade.TradeController;
import com.six.homework.supertrader.entities.Order;
import com.six.homework.supertrader.entities.Security;
import com.six.homework.supertrader.entities.Trade;
import com.six.homework.supertrader.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class OrderControllerTests extends AbstractTestClass {
    @Autowired
    OrderController orderController;

    @Autowired
    TradeController tradeController;

    @Test
    public void createOrderNominal() {
        User user = findAnyUser();
        Security security = findAnySecurity();
        Order nominalOrder = new Order(user.getId(),
                security.getId(),
                "buy",
                BigDecimal.valueOf(12.58),
                30);

        Order savedOrder = orderController.createOrder(nominalOrder);
        nominalOrder.setId(savedOrder.getId());

        assert(nominalOrder.equals(savedOrder));
    }

    @Test
    public void createOrderNonExistentUser() {
        Security security = findAnySecurity();
        Order badOrder = new Order(123L,
                security.getId(),
                "buy",
                BigDecimal.valueOf(12.58),
                30);

        assertThrows(InvalidOrderException.class, () -> orderController.createOrder(badOrder));
    }

    @Test
    public void createOrderNonExistentSecurity() {
        User user = findAnyUser();
        Order badOrder = new Order(user.getId(),
                123L,
                "buy",
                BigDecimal.valueOf(12.58),
                30);

        assertThrows(InvalidOrderException.class, () -> orderController.createOrder(badOrder));
    }

    @Test
    public void createOrderNegativeQuantity() {
        User user = findAnyUser();
        Security security = findAnySecurity();
        Order badOrder = new Order(user.getId(),
                security.getId(),
                "buy",
                BigDecimal.valueOf(12.58),
                -30);

        assertThrows(InvalidOrderException.class, () -> orderController.createOrder(badOrder));
    }

    @Test
    public void createOrderNegativePrice() {
        User user = findAnyUser();
        Security security = findAnySecurity();
        Order badOrder = new Order(user.getId(),
                security.getId(),
                "buy",
                BigDecimal.valueOf(-12.58),
                30);

        assertThrows(InvalidOrderException.class, () -> orderController.createOrder(badOrder));
    }

    @Test
    public void createOrderInvalidType() {
        User user = findAnyUser();
        Security security = findAnySecurity();
        Order badOrder = new Order(user.getId(),
                security.getId(),
                "verkauf",
                BigDecimal.valueOf(12.58),
                30);

        assertThrows(InvalidOrderException.class, () -> orderController.createOrder(badOrder));
    }

    // This part tests how the TradeResolver and OrderController interact - you could call it integration testing

    // The policy is to always take seller's price, no matter who initiates the trade. We do this to simplify.
    @Test
    public void resolvingBuyerInitiativeTrade() {
        int tradeCount = tradeController.getTrades().size();

        Security security = findAnySecurity();

        User buyingUser = findUserByNameOrCreateIt("Diamond");

        Order buyingOrder = new Order(buyingUser.getId(),
                security.getId(),
                "buy",
                BigDecimal.valueOf(101),
                50);

        Order createdBuyingOrder = orderController.createOrder(buyingOrder);

        User sellingUser = findUserByNameOrCreateIt("Paper");

        Order sellingOrder = new Order(sellingUser.getId(),
                security.getId(),
                "sell",
                BigDecimal.valueOf(100),
                100);

        Order createdSellingOrder = orderController.createOrder(sellingOrder);

        Long cboId = createdBuyingOrder.getId();
        Long csoId = createdSellingOrder.getId();
        Trade correspondingTrade = tradeController.getTrades().stream()
                .filter(t -> t.getBuyOrderId().equals(cboId) &&
                        t.getSellOrderId().equals(csoId))
                .findFirst().orElseThrow();

        // Checking that orders are properly updated (quantity and fulfilled should move)
        createdSellingOrder = orderController.getOrder(csoId);
        assert(createdSellingOrder.getFulfilled() == 50);
        assert(createdSellingOrder.getQuantity() == 50);

        createdBuyingOrder = orderController.getOrder(cboId);
        assert(createdBuyingOrder.getFulfilled() == 50);
        assert(createdBuyingOrder.getQuantity() == 0);

        // Checking trade
        assert(tradeController.getTrades().size() == tradeCount + 1);
        assert(correspondingTrade.getQuantity() == 50);
        assert(correspondingTrade.getPrice().compareTo(BigDecimal.valueOf(100)) == 0);
    }

    @Test
    public void orderTriggersMultipleTrades() {
        int tradeCount = tradeController.getTrades().size();

        Security security = findAnySecurity();

        User buyingUser = findUserByNameOrCreateIt("Diamond");

        Order firstBuyingOrder = new Order(buyingUser.getId(),
                security.getId(),
                "buy",
                BigDecimal.valueOf(100),
                50);

        firstBuyingOrder = orderController.createOrder(firstBuyingOrder);

        Order secondBuyingOrder = new Order(buyingUser.getId(),
                security.getId(),
                "buy",
                BigDecimal.valueOf(90),
                50);

        secondBuyingOrder = orderController.createOrder(secondBuyingOrder);

        User sellingUser = findUserByNameOrCreateIt("Paper");

        Order sellingOrder = new Order(sellingUser.getId(),
                security.getId(),
                "sell",
                BigDecimal.valueOf(70),
                90);

        sellingOrder = orderController.createOrder(sellingOrder);
        Long sellingOrderId = sellingOrder.getId();

        List<Trade> allTrades = tradeController.getTrades();
        List<Trade> correspondingTrades = allTrades.stream()
                .filter(t -> t.getSellOrderId().equals(sellingOrderId))
                .toList();

        // Two new trades should result from the selling order creation
        assert(allTrades.size() == tradeCount + 2);
        assert(correspondingTrades.size() == 2);

        // First buying order should be fully fulfilled, because it was the highest bid
        firstBuyingOrder = orderController.getOrder(firstBuyingOrder.getId());
        assert(firstBuyingOrder.getQuantity() == 0);
        assert(firstBuyingOrder.getFulfilled() == 50);

        // Second buying order should be only partially fulfilled
        secondBuyingOrder = orderController.getOrder(secondBuyingOrder.getId());
        assert(secondBuyingOrder.getQuantity() == 10);
        assert(secondBuyingOrder.getFulfilled() == 40);

        // One trade should result from the first buying order
        Long fboId = firstBuyingOrder.getId();
        Trade fboTrade = correspondingTrades.stream()
                .filter(trade -> trade.getBuyOrderId().equals(fboId))
                .findFirst().orElseThrow();

        assert(fboTrade.getSellOrderId().equals(sellingOrderId));
        assert(fboTrade.getQuantity() == 50);
        assert(fboTrade.getPrice().compareTo(BigDecimal.valueOf(70)) == 0);

        // The other trade should result from the second buying order
        Long sboId = secondBuyingOrder.getId();
        Trade sboTrade = correspondingTrades.stream()
                .filter(trade -> trade.getBuyOrderId().equals(sboId))
                .findFirst().orElseThrow();

        assert(sboTrade.getSellOrderId().equals(sellingOrderId));
        assert(sboTrade.getQuantity() == 40);
        assert(sboTrade.getPrice().compareTo(BigDecimal.valueOf(70)) == 0);
    }

    @Test
    public void createNonMatchingOrders() {
        int tradeCount = tradeController.getTrades().size();

        Security security = findAnySecurity();

        User buyingUser = findUserByNameOrCreateIt("Diamond");
        Order buyingOrder = new Order(buyingUser.getId(),
                security.getId(),
                "buy",
                BigDecimal.valueOf(100),
                50);

        orderController.createOrder(buyingOrder);

        User sellingUser = findUserByNameOrCreateIt("Paper");
        Order sellingOrder = new Order(sellingUser.getId(),
                security.getId(),
                "sell",
                BigDecimal.valueOf(10000),
                50);

        orderController.createOrder(sellingOrder);

        // Such two orders should never result in a trade
        assert(tradeController.getTrades().size() == tradeCount);
    }
}
