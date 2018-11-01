package com.example.demo.component.policy;

import com.example.demo.component.policy.PolicyInstanceHasTblVehicleType;
import com.example.demo.component.vehicleType.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PolicyInstanceHasVehicleTypeRepository extends JpaRepository<PolicyInstanceHasTblVehicleType, Integer> {

    List<PolicyInstanceHasTblVehicleType> findAllByPolicyInstanceId(Integer policyInstanceId);

    Optional<PolicyInstanceHasTblVehicleType> findByPolicyInstanceIdAndVehicleTypeId(Integer policyId, VehicleType vehicleType);
}
