package com.example.demo.repository;

import com.example.demo.entity.PolicyHasTblVehicleType;
import com.example.demo.entity.PolicyInstance;
import com.example.demo.entity.PolicyInstanceHasTblVehicleType;
import com.example.demo.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyInstanceHasVehicleTypeRepository extends JpaRepository<PolicyInstanceHasTblVehicleType, Integer> {

    List<PolicyInstanceHasTblVehicleType> findAllByPolicyInstanceId(Integer policyInstanceId);

    Optional<PolicyInstanceHasTblVehicleType> findByPolicyInstanceIdAndVehicleTypeId(Integer policyId, VehicleType vehicleType);
}
