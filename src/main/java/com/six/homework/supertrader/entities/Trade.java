package com.six.homework.supertrader.entities;

import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class Trade extends AbstractEntity {
    private Long sellOrderId;

    private Long buyOrderId;

    private BigDecimal price;

    private int quantity;

    public Trade() {}

    public Trade(Long sellOrderId, Long buyOrderId, BigDecimal price, int quantity) {
        this.sellOrderId = sellOrderId;
        this.buyOrderId = buyOrderId;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getSellOrderId() {
        return sellOrderId;
    }

    public Long getBuyOrderId() {
        return buyOrderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
