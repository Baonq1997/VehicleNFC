package com.example.demo.component.user;

import com.example.demo.component.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndPassword(String phone, String password);

    Optional<User> findByVehicleNumber(String vehicleNumber);
}
