package com.example.demo.controller;

import com.example.demo.entities.OrderStatus;
import com.example.demo.service.OrderStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/transaction/status")
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<Optional<OrderStatus>> getTransactionStatusById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderStatusService.getOrderStatusById(id));
    }
}