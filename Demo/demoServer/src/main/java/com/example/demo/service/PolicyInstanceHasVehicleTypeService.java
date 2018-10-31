package com.example.demo.service;

import com.example.demo.repository.PolicyInstanceHasVehicleTypeRepository;
import com.example.demo.repository.PolicyInstanceRepository;
import org.springframework.stereotype.Service;

@Service
public class PolicyInstanceHasVehicleTypeService {
    private final PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository;

    public PolicyInstanceHasVehicleTypeService(PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository) {
        this.policyInstanceHasVehicleTypeRepository = policyInstanceHasVehicleTypeRepository;
    }
}
