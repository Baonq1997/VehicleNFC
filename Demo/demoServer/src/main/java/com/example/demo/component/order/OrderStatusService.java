package com.example.demo.component.order;

import com.example.demo.component.order.OrderStatus;
import com.example.demo.component.order.OrderStatusRepository;
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
