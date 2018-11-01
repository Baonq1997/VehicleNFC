package com.example.demo.component.order;

import com.example.demo.component.order.OrderPricing;
import com.example.demo.component.order.OrderPricingRepository;
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
