package com.example.demo.component.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefundRequestRepository extends JpaRepository<RefundRequest, Integer> {

//    Optional<RefundRequest> findByName(String name);
}
