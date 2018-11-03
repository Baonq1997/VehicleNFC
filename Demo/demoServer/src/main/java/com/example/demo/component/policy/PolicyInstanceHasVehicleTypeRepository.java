package com.example.demo.component.policy;

import com.example.demo.component.policy.PolicyInstanceHasTblVehicleType;
import com.example.demo.component.vehicleType.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PolicyInstanceHasVehicleTypeRepository extends JpaRepository<PolicyInstanceHasTblVehicleType, Integer> {

    List<PolicyInstanceHasTblVehicleType> findAllByPolicyInstanceId(Integer policyInstanceId);

    Optional<PolicyInstanceHasTblVehicleType> findByPolicyInstanceIdAndVehicleTypeId(Integer policyInstanceId, VehicleType vehicleType);

//    void deleteByIdAndPricingList(Integer policyInstanceHasTblVehicleTypeId, List<Pricing> pricings);
//    void deleteByPricingList(List<Pricing> pricings);
//    @Modifying
//    @Query(value = "DELETE FROM vehiclenfc.tbl_policy_instance_has_tbl_vehicle_type_has_tbl_pricing WHERE tbl_policy_instance_has_tbl_vehicle_type_id = :instanceId AND tbl_pricing_id = :pricingId"
//    , nativeQuery = true)
//    void deletePolicyInstancePricing(@Param("instanceId") Integer id, @Param("pricingId") Integer pricingId);

}