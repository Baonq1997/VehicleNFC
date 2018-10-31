package com.example.demo.service;

import com.example.demo.entity.PolicyInstanceHasTblVehicleType;
import com.example.demo.entity.Pricing;
import com.example.demo.repository.PolicyInstanceHasVehicleTypeRepository;
import com.example.demo.repository.PricingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PricingService {
    private final PricingRepository pricingRepository;
    private final PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository;

    public PricingService(PricingRepository pricingRepository, PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository) {
        this.pricingRepository = pricingRepository;
        this.policyInstanceHasVehicleTypeRepository = policyInstanceHasVehicleTypeRepository;
    }

    public Pricing findByPolicyHasVehicleTypeId(Integer policyHasVehicleTypeId) {
        Optional<PolicyInstanceHasTblVehicleType> policyHasTblVehicleType = policyInstanceHasVehicleTypeRepository.findById(policyHasVehicleTypeId);
        if (policyHasTblVehicleType.isPresent()) {
            PolicyInstanceHasTblVehicleType policyHasTblVehicleTypeDB = policyHasTblVehicleType.get();
            //TODO
//            Pricing pricing = pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleTypeDB.getId());
            Pricing pricing = null;
            return pricing;
        }
        return null;
    }

    //Todo
    public List<Pricing> findAllByPolicyHasTblVehicleTypeId(Integer policyHasTblVehicleTypeId) {
        Optional<PolicyInstanceHasTblVehicleType> policyHasTblVehicleType = policyInstanceHasVehicleTypeRepository.findById(policyHasTblVehicleTypeId);
        if (policyHasTblVehicleType.isPresent()) {
            PolicyInstanceHasTblVehicleType policyHasTblVehicleTypeDB = policyHasTblVehicleType.get();
//            List<Pricing> pricings = pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleTypeDB.getId());
//            return pricings;
            return null;
        }
        return null;
    }

    public Pricing save(Pricing pricing) {
        return pricingRepository.save(pricing);
    }

    public void deletePricing(Integer id) {
        pricingRepository.deleteById(id);
    }

    public void deleteByPolicyHasTblVehicleTypeId(Integer policyHasVehicleTypeId) {
//        pricingRepository.deleteByPolicyHasTblVehicleTypeId(policyHasVehicleTypeId);
    }

    public Pricing findById(Integer pricingId) {
        return pricingRepository.findById(pricingId).get();
    }
}
