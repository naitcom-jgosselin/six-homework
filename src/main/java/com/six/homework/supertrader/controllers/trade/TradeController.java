package com.six.homework.supertrader.controllers.trade;

import com.six.homework.supertrader.entities.Trade;
import com.six.homework.supertrader.repositories.TradeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TradeController {

    private final TradeRepository tradeRepository;
    public TradeController(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }
    @GetMapping("/trades")
    public List<Trade> getTrades() {
        return tradeRepository.findAll();
    }
}
