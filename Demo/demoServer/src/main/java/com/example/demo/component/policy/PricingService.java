package com.example.demo.component.policy;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PricingService {
    private final PricingRepository pricingRepository;
    private final PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository;

    public PricingService(PricingRepository pricingRepository, PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository) {
        this.pricingRepository = pricingRepository;
        this.policyHasVehicleTypeRepository = policyHasVehicleTypeRepository;
    }

    public Pricing findByPolicyHasVehicleTypeId(Integer policyHasVehicleTypeId) {
        Optional<PolicyHasTblVehicleType> policyHasTblVehicleType = policyHasVehicleTypeRepository.findById(policyHasVehicleTypeId);
        if (policyHasTblVehicleType.isPresent()) {
            PolicyHasTblVehicleType policyHasTblVehicleTypeDB = policyHasTblVehicleType.get();
            //TODO
//            Pricing pricing = pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleTypeDB.getId());
            Pricing pricing = null;
            return pricing;
        }
        return null;
    }

    //Todo
    public List<Pricing> findAllByPolicyHasTblVehicleTypeId(Integer policyHasTblVehicleTypeId) {
        Optional<PolicyHasTblVehicleType> policyHasTblVehicleType = policyHasVehicleTypeRepository.findById(policyHasTblVehicleTypeId);
        if (policyHasTblVehicleType.isPresent()) {
            PolicyHasTblVehicleType policyHasTblVehicleTypeDB = policyHasTblVehicleType.get();
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
