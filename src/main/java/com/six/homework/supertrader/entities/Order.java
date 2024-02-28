package com.six.homework.supertrader.entities;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Order extends AbstractEntity {

    // TODO : there has to be a specific annotation for foreign keys
    private Long userId;
    private Long securityId;
    private String type;
    private BigDecimal price;
    private int quantity;
    private int fulfilled;

    public Order() {}

    public Order(Long userId, Long securityId, String type, BigDecimal price, int quantity) {
        this.userId = userId;
        this.securityId = securityId;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.fulfilled = 0;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getSecurityId() {
        return securityId;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(int fulfilled) {
        this.fulfilled = fulfilled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return quantity == order.quantity &&
                fulfilled == order.fulfilled &&
                Objects.equals(userId, order.userId) &&
                Objects.equals(securityId, order.securityId) &&
                Objects.equals(type, order.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, securityId, type, quantity, fulfilled);
    }
}
