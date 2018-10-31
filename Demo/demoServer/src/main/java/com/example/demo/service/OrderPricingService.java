package com.example.demo.service;

import com.example.demo.entity.OrderPricing;
import com.example.demo.repository.OrderPricingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderPricingService {
    private final OrderPricingRepository orderPricingRepository;

    public OrderPricingService(OrderPricingRepository orderPricingRepository) {
        this.orderPricingRepository = orderPricingRepository;
    }

    public List<OrderPricing> getByOrderId(Integer orderId) {
        return orderPricingRepository.findByOrderId(orderId);
    }
}
