package com.example.demo;

import com.example.demo.component.order.Order;
import com.example.demo.component.order.OrderService;
import com.example.demo.component.user.User;
import com.example.demo.component.user.UserService;
import com.example.demo.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class DemoApplication {

    static OrderService orderService;
    static UserService userService;

    public DemoApplication(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        ThreadService threadService = new ThreadService();
        threadService.setOrderService(orderService);
        threadService.setUserService(userService);
        threadService.start();
    }
}
