package com.example.demo.component.policy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {

//    Optional<Policy> findByPolicyIdAndVehicleTypeId(Integer policyId, VehicleType vehicleType);

    //
//    List<PolicyHasTblVehicleType> findByPolicyIdIn(List<Policy> policyList);
//
    List<Policy> findAllByLocationId(Integer locationId);

    @Modifying
    @Query(
            value = "DELETE FROM tbl_policy_has_tbl_vehicle_type where id= :id",
            nativeQuery = true)
    void deletePolicyHasVehicleById(@Param("id") Integer id);
}
