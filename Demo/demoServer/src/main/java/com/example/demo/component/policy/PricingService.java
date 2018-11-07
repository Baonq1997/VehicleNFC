package com.example.demo.component.policy;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PricingService {
    private final PricingRepository pricingRepository;
    private final PolicyRepository policyRepository;

    public PricingService(PricingRepository pricingRepository, PolicyRepository policyRepository) {
        this.pricingRepository = pricingRepository;
        this.policyRepository = policyRepository;
    }

    public List<Pricing> findByPolicyHasVehicleTypeId(Integer policyHasVehicleTypeId) {
        Optional<Policy> policy =
                policyRepository.findById(policyHasVehicleTypeId);
        if (policy.isPresent()) {
            Policy policyDB = policy.get();
            //TODO
//            Pricing pricing = pricingRepository.findAllByPolicyHasTblVehicleTypeId(policyHasTblVehicleTypeDB.getId());
            List<Pricing> pricing = policyDB.getPricings();
            return pricing;
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
