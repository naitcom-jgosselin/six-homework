package com.six.homework.supertrader.resolvers;

import com.six.homework.supertrader.entities.Order;
import com.six.homework.supertrader.entities.Trade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TradeResolverTests {
    @Autowired
    TradeResolver tradeResolver;

    @Test
    public void tradeScenarioNominal() {
        Order buyingOrder =
                new Order(1L, 1L, "buy", BigDecimal.valueOf(50.00), 50);
        buyingOrder.setId(1L);

        Order sellingOrder =
                new Order(2L, 1L, "sell", BigDecimal.valueOf(45.00), 20);
        sellingOrder.setId(2L);

        Trade trade = tradeResolver.createTrade(buyingOrder, sellingOrder);

        // Trade
        assert(trade.getBuyOrderId().equals(buyingOrder.getId()));
        assert(trade.getSellOrderId().equals(sellingOrder.getId()));
        assert(trade.getPrice().equals(sellingOrder.getPrice()));
        assertEquals(trade.getQuantity(), sellingOrder.getFulfilled());
    }
}
