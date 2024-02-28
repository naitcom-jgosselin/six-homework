package com.six.homework.supertrader.controllers.order;

import com.six.homework.supertrader.entities.Order;
import com.six.homework.supertrader.repositories.OrderRepository;
import com.six.homework.supertrader.repositories.SecurityRepository;
import com.six.homework.supertrader.repositories.TradeRepository;
import com.six.homework.supertrader.repositories.UserRepository;
import com.six.homework.supertrader.resolvers.TradeResolver;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.BorderUIResource;
import java.math.BigDecimal;
import java.util.List;

@RestController
public class OrderController {

    private final OrderRepository repository;
    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final TradeResolver tradeResolver;
    public OrderController(OrderRepository repository,
                           TradeRepository tradeRepository,
                           UserRepository userRepository,
                           SecurityRepository securityRepository,
                           TradeResolver tradeResolver) {
        this.repository = repository;
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.tradeResolver = tradeResolver;
    }

    // More or less for debugging E2E tests
    @GetMapping("/orders")
    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    @GetMapping("/orders/{id}")
    public Order getOrder(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestBody Order order) {
        // Check-tower of doom
        if(order.getQuantity() <= 0) {
            throw new InvalidOrderException("Order quantity should be superior to 0 at creation");
        }

        if(order.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderException("Order price should be superior to 0");
        }

        if(!order.getType().equals("buy") && !order.getType().equals("sell")) {
            throw new InvalidOrderException("Order type should be either 'buy' or 'sell'");
        }

        if(securityRepository.findById(order.getSecurityId()).isEmpty()) {
            throw new InvalidOrderException("Order refers to a non-existent security");
        }

        if(userRepository.findById(order.getUserId()).isEmpty()) {
            throw new InvalidOrderException("Order refers to a non-existent user");
        }

        // Save first, so that the order receives an ID
        Order createdOrder = repository.save(order);

        // Then, try to resolve it against the existing matching orders - if any
        Order resolvedOrder = tradeResolver.tryResolve(createdOrder, repository, tradeRepository);

        // Then save the resolved order
        return repository.save(resolvedOrder);
    }

}
