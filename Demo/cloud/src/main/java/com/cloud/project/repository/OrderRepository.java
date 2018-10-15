package com.cloud.project.repository;

import com.cloud.project.model.Order;
import com.cloud.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
