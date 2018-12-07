package com.example.demo.component.user;

import com.example.demo.component.user.User;
import com.example.demo.component.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumberAndPassword(String phone, String password);

    Optional<User> findByVehicle(Vehicle vehicle);
//
//    @Query(value = "SELECT * FROM tbl_user WHERE id = :id", nativeQuery = true)
//    Optional<User> findUserById(@Param("id") Integer id);

    @Modifying
    @Query(value = "UPDATE tbl_user SET is_activated = :status where id = :id", nativeQuery = true)
    void updateUserStatus(@Param("status") long status,@Param("id") Integer id);

}
