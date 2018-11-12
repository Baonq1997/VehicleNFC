package com.example.demo.component.policy;

import com.example.demo.component.policy.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {
    List<Pricing> findByPolicyHasTblVehicleTypeId(Integer policyHasTblVehicleType);

    @Query(value = "SELECT * FROM tbl_pricing WHERE tbl_policy_has_tbl_vehicle_type_id = :policyHasTblVehicleType"
    ,nativeQuery = true)
    List<Pricing> findPricingByPolicyVehicle(@Param("policyHasTblVehicleType") Integer policyHasTblVehicleType);

    @Modifying
    @Query(value = "DELETE FROM tbl_pricing WHERE id = :id",
     nativeQuery = true)
    void deletePricingById(@Param("id") Integer id);
//    @Modifying
//    @Query(value = "DELETE FROM tbl_pricing WHERE id = :id", nativeQuery = true)
//    void deletePricingById(@Param("id") Integer id);

//    Pricing findByPolicyHasTblVehicleTypeId(PolicyHasTblVehicleType policyHasTblVehicleType);

//    List<Pricing> findAllByPolicyHasTblVehicleTypeId(Integer policyHasTblVehicleTypeId);\

    void deleteByPolicyHasTblVehicleTypeId(Integer policyHasVehicleId);
//
//    void deleteByPolicyHasTblVehicleTypeIdIn(List<Integer> listPolicyHasTblVehicleTypeId);
}
