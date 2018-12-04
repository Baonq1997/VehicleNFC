package com.example.demo.component.policy;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PolicyHasVehicleTypeService {
    private final PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository;
    private final PricingRepository pricingRepository;

    public PolicyHasVehicleTypeService(PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository, PricingRepository pricingRepository) {
        this.policyHasVehicleTypeRepository = policyHasVehicleTypeRepository;
        this.pricingRepository = pricingRepository;
    }
    public List<PolicyHasTblVehicleType> getByPolicyId(Integer policyInstanceId) {
        List<PolicyHasTblVehicleType> policyHasTblVehicleTypes
                = policyHasVehicleTypeRepository.findAllByPolicyId(policyInstanceId);

        for (PolicyHasTblVehicleType policyHasTblVehicleType : policyHasTblVehicleTypes) {
            List<Pricing> pricings = pricingRepository.findPricingByPolicyVehicle(policyHasTblVehicleType.getId());
            if (pricings != null) {
                policyHasTblVehicleType.setPricingList(pricings);
            }

        }
        return policyHasTblVehicleTypes;
    }

    public PolicyHasTblVehicleType saveMinHour(Integer minHour, Integer policyHasVehicleTypeId) {
        Optional<PolicyHasTblVehicleType> policyHasTblVehicleTypeOpt = policyHasVehicleTypeRepository.findById(policyHasVehicleTypeId);
        if (policyHasTblVehicleTypeOpt.isPresent()) {
            PolicyHasTblVehicleType policyHasTblVehicleType = policyHasTblVehicleTypeOpt.get();
            policyHasTblVehicleType.setMinHour(minHour);
            policyHasVehicleTypeRepository.save(policyHasTblVehicleType);
            return policyHasTblVehicleType;
        }
        return null;
    }
}
