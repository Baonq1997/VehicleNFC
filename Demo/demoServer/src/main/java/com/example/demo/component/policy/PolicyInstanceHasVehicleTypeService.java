package com.example.demo.component.policy;

import com.example.demo.component.policy.PolicyInstanceHasVehicleTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyInstanceHasVehicleTypeService {
    private final PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository;
    private final PricingRepository pricingRepository;

    public PolicyInstanceHasVehicleTypeService(PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository, PricingRepository pricingRepository) {
        this.policyInstanceHasVehicleTypeRepository = policyInstanceHasVehicleTypeRepository;
        this.pricingRepository = pricingRepository;
    }
    public List<PolicyInstanceHasTblVehicleType> getByPolicyInstanceId(Integer policyInstanceId) {
        List<PolicyInstanceHasTblVehicleType> policyInstanceHasTblVehicleTypes
                = policyInstanceHasVehicleTypeRepository.findAllByPolicyInstanceId(policyInstanceId);

        for (PolicyInstanceHasTblVehicleType policyInstanceHasTblVehicleType:policyInstanceHasTblVehicleTypes) {
            List<Pricing> pricings = pricingRepository.findPricingByPolicyInstanceVehicle(policyInstanceHasTblVehicleType.getId());
            if (pricings != null) {
                policyInstanceHasTblVehicleType.setPricingList(pricings);
            }

        }
        return policyInstanceHasTblVehicleTypes;
    }
}
