package com.cloud.project.repository;

import com.cloud.project.model.Order;
import com.cloud.project.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {

}
