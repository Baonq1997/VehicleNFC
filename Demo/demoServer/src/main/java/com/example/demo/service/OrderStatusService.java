package com.example.demo.service;

import com.example.demo.entity.OrderStatus;
import com.example.demo.repository.OrderStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderStatusService {
    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    public Optional<OrderStatus> getOrderStatusById(Integer id){
        return orderStatusRepository.findById(id);
    }
}
