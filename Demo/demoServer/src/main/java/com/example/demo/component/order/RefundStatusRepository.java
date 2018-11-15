package com.example.demo.component.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefundStatusRepository extends JpaRepository<RefundStatus, Integer> {

    Optional<RefundStatus> findByName(String name);
}
