package com.example.demo.repository;

import com.example.demo.entity.Location;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderStatus;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByUserIdAndLocationId(User user, Location location);

    Optional<Order> findByUserIdAndLocationIdAndOrderStatusId(User user, Location location, OrderStatus orderStatus);

    List<Order> findByUserIdOrderByCheckInDateDesc(User user);

    Optional<Order> findFirstByUserIdAndOrderStatusId(User user, OrderStatus OrderStatus);
}
