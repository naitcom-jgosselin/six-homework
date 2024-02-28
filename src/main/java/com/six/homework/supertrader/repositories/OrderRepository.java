package com.six.homework.supertrader.repositories;

import com.six.homework.supertrader.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
