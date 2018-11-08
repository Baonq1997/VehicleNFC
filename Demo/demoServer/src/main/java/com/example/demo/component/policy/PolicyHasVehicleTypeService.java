package com.example.demo.component.policy;

import org.springframework.stereotype.Service;

@Service
public class PolicyHasVehicleTypeService {
    private final PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository;

    public PolicyHasVehicleTypeService(PolicyHasVehicleTypeRepository policyHasVehicleTypeRepository) {
        this.policyHasVehicleTypeRepository = policyHasVehicleTypeRepository;
    }
}
