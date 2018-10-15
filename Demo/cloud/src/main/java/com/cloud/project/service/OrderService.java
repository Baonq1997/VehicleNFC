package com.cloud.project.service;

import com.cloud.project.model.Gundam;
import com.cloud.project.model.Order;
import com.cloud.project.model.User;
import com.cloud.project.repository.GundamRepository;
import com.cloud.project.repository.OrderRepository;
import com.cloud.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final GundamRepository gundamRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;


    public OrderService(UserRepository userRepository, GundamRepository gundamRepository, OrderRepository orderRepository, OrderDetailService orderDetailService) {
        this.userRepository = userRepository;
        this.gundamRepository = gundamRepository;
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
    }

    public Order createOrder(String username, Map<Gundam, Integer> cart) {
        Order order = new Order();
        User user = userRepository.findByUsername(username).get();
        order.setUser(user);
        order.setStartDate(new Date().getTime());

        double total = 0;
        for (Map.Entry<Gundam, Integer> entry : cart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        order.setTotal(total);
        orderRepository.save(order);

        for (Map.Entry<Gundam, Integer> entry : cart.entrySet()) {
            orderDetailService.createOrderDetail(order,entry.getKey(),entry.getValue());
        }

        return order;
    }

}
