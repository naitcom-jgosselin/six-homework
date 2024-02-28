package com.six.homework.supertrader.resolvers;

import com.six.homework.supertrader.entities.Order;
import com.six.homework.supertrader.entities.Trade;
import com.six.homework.supertrader.repositories.OrderRepository;
import com.six.homework.supertrader.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TradeResolver {

    public Order tryResolve(Order order, OrderRepository orderRepository, TradeRepository tradeRepository) {
        // If there are matches, create corresponding trade(s)
        // The policy is the following :
        // 1 - Sell policy is sell to highest bidder first, then go to second highest etc.
        // 2 - Buy policy is buy to lowest bidder first, then go to second lowest etc.

        if(order.getType().equals("sell")) {
            // Need to do some weird wrapping to soothe IntelliJ
            List<Order> eligibleOrders = new ArrayList<>(
                    orderRepository.findAll().stream()
                            .filter(o -> o.getType().equals("buy"))
                            .filter(o -> o.getSecurityId().equals(order.getSecurityId()))
                            .filter(o -> o.getPrice().compareTo(order.getPrice()) >= 0)
                            .sorted(Comparator.comparing(Order::getPrice))
                            .toList()
            );

            Collections.reverse(eligibleOrders);
            // TODO : fix code duplication here
            int i = 0;
            while(order.getQuantity() > 0 && i<eligibleOrders.size()) {
                Order matchedBuyingOrder = eligibleOrders.get(i);

                Trade resultingTrade = createTrade(matchedBuyingOrder, order);

                // Update orders according to the resulting trade
                orderRepository.save(matchedBuyingOrder);
                orderRepository.save(order);

                // Save the trade, and proceed
                tradeRepository.save(resultingTrade);

                i++;
            }
        } else {
            // Eligible selling orders, sorted by price ascending
            List<Order> eligibleOrders = orderRepository.findAll().stream()
                    .filter(o -> o.getType().equals("sell"))
                    .filter(o -> o.getSecurityId().equals(order.getSecurityId()))
                    .filter(o -> o.getPrice().compareTo(order.getPrice()) <= 0)
                    .sorted(Comparator.comparing(Order::getPrice))
                    .toList();

            int i = 0;
            while(order.getQuantity() > 0 && i<eligibleOrders.size()) {
                Order matchedSellingOrder = eligibleOrders.get(i);

                Trade resultingTrade = createTrade(order, matchedSellingOrder);

                // Update orders according to the resulting trade
                orderRepository.save(matchedSellingOrder);
                orderRepository.save(order);

                // Save the trade, and proceed
                tradeRepository.save(resultingTrade);

                i++;
            }
        }
        return order;
    }

    // TODO : this method assumes that the buying and selling order have been checked to be compatible beforehand
    // TODO : if enough time find a way to make this more secure
    public Trade createTrade(Order buyingOrder, Order sellingOrder) {
        int tradedQuantity = 0;

        // TODO : find a less embarrassing way of doing this
        if(buyingOrder.getQuantity() <= sellingOrder.getQuantity()) {
            tradedQuantity = buyingOrder.getQuantity();
        } else {
            tradedQuantity = sellingOrder.getQuantity();
        }

        // Updating orders
        buyingOrder.setQuantity(buyingOrder.getQuantity() - tradedQuantity);
        buyingOrder.setFulfilled(buyingOrder.getFulfilled() + tradedQuantity);

        sellingOrder.setQuantity(sellingOrder.getQuantity() - tradedQuantity);
        sellingOrder.setFulfilled(sellingOrder.getFulfilled() + tradedQuantity);

        return new Trade(sellingOrder.getId(),
                buyingOrder.getId(),
                sellingOrder.getPrice(),
                tradedQuantity
        );
    }
}
