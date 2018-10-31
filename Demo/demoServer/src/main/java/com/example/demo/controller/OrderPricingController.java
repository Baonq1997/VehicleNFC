package com.example.demo.controller;

import com.example.demo.entity.OrderPricing;
import com.example.demo.service.OrderPricingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/order-pricing")
public class OrderPricingController {
    private final OrderPricingService orderPricingService;

    public OrderPricingController(OrderPricingService orderPricingService) {
        this.orderPricingService = orderPricingService;
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getOrderPricingByOrderId(@PathVariable("id") Integer id) {
        List<OrderPricing> orderPricingList = orderPricingService.getByOrderId(id);
        return !orderPricingList.isEmpty() ? ResponseEntity.status(HttpStatus.OK).body(orderPricingList) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
