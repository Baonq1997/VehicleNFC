package com.example.demo.component.policy;

import com.example.demo.component.policy.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {

    @Modifying
    @Query(value = "DELETE FROM tbl_pricing WHERE id = :id", nativeQuery = true)
    void deletePricingById(@Param("id") Integer id);

//    Pricing findByPolicyHasTblVehicleTypeId(PolicyHasTblVehicleType policyHasTblVehicleType);

//    List<Pricing> findAllByPolicyHasTblVehicleTypeId(Integer policyHasTblVehicleTypeId);\

//    void deleteByPolicyHasTblVehicleTypeId(Integer policyHasTblVehicleTypeId);
//
//    void deleteByPolicyHasTblVehicleTypeIdIn(List<Integer> listPolicyHasTblVehicleTypeId);
}
