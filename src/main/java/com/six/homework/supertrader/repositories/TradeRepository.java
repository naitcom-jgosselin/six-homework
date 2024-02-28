package com.six.homework.supertrader.repositories;

import com.six.homework.supertrader.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {
}
