package com.cloud.project.service;

import com.cloud.project.model.Gundam;
import com.cloud.project.model.Order;
import com.cloud.project.model.OrderDetail;
import com.cloud.project.model.User;
import com.cloud.project.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService {
    private final UserRepository userRepository;

    public OrderDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password).get();
    }

    public OrderDetail createOrderDetail(Order order, Gundam gundam, int quantity){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrder(order);
        orderDetail.setGundam(gundam);
        orderDetail.setQuantity(quantity);
        return orderDetail;
    }

}
