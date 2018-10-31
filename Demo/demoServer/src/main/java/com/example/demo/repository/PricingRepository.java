package com.example.demo.repository;

import com.example.demo.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {
//    Pricing findByPolicyHasTblVehicleTypeId(PolicyHasTblVehicleType policyHasTblVehicleType);

//    List<Pricing> findAllByPolicyHasTblVehicleTypeId(Integer policyHasTblVehicleTypeId);\

//    void deleteByPolicyHasTblVehicleTypeId(Integer policyHasTblVehicleTypeId);
//
//    void deleteByPolicyHasTblVehicleTypeIdIn(List<Integer> listPolicyHasTblVehicleTypeId);
}
