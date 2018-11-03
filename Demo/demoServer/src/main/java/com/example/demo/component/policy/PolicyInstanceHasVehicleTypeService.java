package com.example.demo.component.policy;

import com.example.demo.component.policy.PolicyInstanceHasVehicleTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyInstanceHasVehicleTypeService {
    private final PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository;

    public PolicyInstanceHasVehicleTypeService(PolicyInstanceHasVehicleTypeRepository policyInstanceHasVehicleTypeRepository) {
        this.policyInstanceHasVehicleTypeRepository = policyInstanceHasVehicleTypeRepository;
    }
    public List<PolicyInstanceHasTblVehicleType> getByPolicyInstanceId(Integer policyInstanceId) {
        return policyInstanceHasVehicleTypeRepository.findAllByPolicyInstanceId(policyInstanceId);
    }
}
