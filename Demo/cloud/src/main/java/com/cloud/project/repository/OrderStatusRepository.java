package com.cloud.project.repository;

import com.cloud.project.model.Order;
import com.cloud.project.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    
}
