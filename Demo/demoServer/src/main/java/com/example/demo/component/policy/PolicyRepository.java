package com.example.demo.component.policy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {

    List<Policy> findAllByLocationId(Integer locationId);

    List<Policy> findByAllowedParkingFromAndAllowedParkingToAndLocationId(Long allowedParkingFrom, Long allowedParkingTo, Integer locationId);
}
