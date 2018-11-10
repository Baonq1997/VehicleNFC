package com.example.demo.component.policy;

import com.example.demo.component.vehicleType.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PolicyHasVehicleTypeRepository extends JpaRepository<PolicyHasTblVehicleType, Integer> {

    List<PolicyHasTblVehicleType> findAllByPolicyId(Integer policyInstanceId);

    Optional<PolicyHasTblVehicleType> findByPolicyIdAndVehicleTypeId(Integer policyId, VehicleType vehicleType);

    @Modifying
    @Query(value = "DELETE FROM tbl_policy_has_tbl_vehicle_type WHERE id = :id"
    , nativeQuery = true)
    void deletePolicyHasVehicleTypeById(@Param("id") Integer id);
//    void deleteByIdAndPricingList(Integer policyInstanceHasTblVehicleTypeId, List<Pricing> pricings);
//    void deleteByPricingList(List<Pricing> pricings);
//    @Modifying
//    @Query(value = "DELETE FROM vehiclenfc.tbl_policy_instance_has_tbl_vehicle_type_has_tbl_pricing WHERE tbl_policy_instance_has_tbl_vehicle_type_id = :instanceId AND tbl_pricing_id = :pricingId"
//    , nativeQuery = true)
//    void deletePolicyInstancePricing(@Param("instanceId") Integer id, @Param("pricingId") Integer pricingId);

}