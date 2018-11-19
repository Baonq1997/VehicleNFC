package com.example.demo.component.order;

import com.example.demo.component.location.Location;
import com.example.demo.component.user.User;
import com.example.demo.component.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

//    Optional<Order> findByUserIdAndLocationId(User user, Location location);

    Optional<Order> findByUserIdAndLocationIdAndOrderStatusId(User user, Integer locationId, OrderStatus orderStatus);

    List<Order> findByUserIdAndVehicleOrderByCheckInDateDesc(User user, Vehicle vehicle);

    Optional<Order> findFirstByUserIdAndOrderStatusId(User user, OrderStatus OrderStatus);

    Optional<Order> findById(Integer orderId);
}
