package com.example.demo.component.policy;

import com.example.demo.component.policy.Policy;
import com.example.demo.component.policy.PolicyHasTblVehicleType;
import com.example.demo.component.vehicleType.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PolicyHasVehicleTypeRepository extends JpaRepository<PolicyHasTblVehicleType, Integer> {
    List<PolicyHasTblVehicleType> findByPolicyId(Integer policyId);

    Optional<PolicyHasTblVehicleType> findByPolicyIdAndVehicleTypeId(Integer policyId, VehicleType vehicleType);

    List<PolicyHasTblVehicleType> findByPolicyIdIn(List<Policy> policyList);

    @Modifying
    @Query(
            value = "DELETE FROM tbl_policy_has_tbl_vehicle_type where id= :id",
            nativeQuery = true)
    void deletePolicyHasVehicleById(@Param("id") Integer id);
}
