package com.example.demo.component.policy;

import com.example.demo.component.policy.PolicyInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyInstanceRepository extends JpaRepository<PolicyInstance, Long> {

    List<PolicyInstance> findAllByLocationId(Integer locationId);
}
