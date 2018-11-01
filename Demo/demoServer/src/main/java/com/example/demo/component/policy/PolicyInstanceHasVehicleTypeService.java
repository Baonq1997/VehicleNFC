package com.example.demo.component.policy;

import com.example.demo.component.policy.PolicyInstanceHasVehicleTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class PolicyInstanceHasVehicleTypeService {
    private final PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository;

    public PolicyInstanceHasVehicleTypeService(PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository) {
        this.policyInstanceHasVehicleTypeRepository = policyInstanceHasVehicleTypeRepository;
    }
}
