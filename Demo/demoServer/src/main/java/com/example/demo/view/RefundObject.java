package com.example.demo.view;

import com.example.demo.entity.Order;
import com.example.demo.entity.User;

import java.io.Serializable;

public class RefundObject implements Serializable {
    private User user;
    private Order order;
    private Double refundMoney;

    public RefundObject() {
    }

    public RefundObject(User user, Order order, double refundMoney) {
        this.user = user;
        this.order = order;
        this.refundMoney = refundMoney;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Double getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(Double refundMoney) {
        this.refundMoney = refundMoney;
    }
}
