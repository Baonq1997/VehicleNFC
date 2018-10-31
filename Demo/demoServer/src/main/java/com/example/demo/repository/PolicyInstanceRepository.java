package com.example.demo.repository;

import com.example.demo.entity.Location;
import com.example.demo.entity.PolicyInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyInstanceRepository extends JpaRepository<PolicyInstance, Long> {

    List<PolicyInstance> findAllByLocationId(Integer locationId);
}
