package com.cloud.project.controller;

import com.cloud.project.model.Gundam;
import com.cloud.project.model.Order;
import com.cloud.project.model.Tag;
import com.cloud.project.service.OrderService;
import com.cloud.project.service.TagService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/order")
public class OrderController {
    //    private final GundamService gundamService;
//    private final UserService userService;
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/check-out")
    public Order checkout(@Param("username") String username, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<Gundam, Integer> cart = (Map<Gundam, Integer>) session.getAttribute(username);
        if (cart != null) {
            return orderService.createOrder(username, cart);
        }
        return null;
    }

}
