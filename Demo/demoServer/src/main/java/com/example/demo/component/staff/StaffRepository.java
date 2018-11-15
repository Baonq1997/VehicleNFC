package com.example.demo.component.staff;

import com.example.demo.component.order.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Optional<Staff> findByUsernameAndPassword(String username, String password);

    Optional<Staff> findByUsername(String username);
}
