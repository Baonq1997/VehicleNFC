package com.example.demo.component.policy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PolicyHasVehicleTypeRepository extends JpaRepository<PolicyHasTblVehicleType, Integer> {

    List<PolicyHasTblVehicleType> findAllByPolicyInstanceId(Integer policyInstanceId);

}
