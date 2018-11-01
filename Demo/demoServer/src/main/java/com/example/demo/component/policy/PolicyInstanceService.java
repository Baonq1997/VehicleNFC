package com.example.demo.component.policy;

import com.example.demo.component.policy.PolicyInstanceRepository;
import org.springframework.stereotype.Service;

@Service
public class PolicyInstanceService {
    private PolicyInstanceRepository policyInstanceRepository;

    public PolicyInstanceService(PolicyInstanceRepository policyInstanceRepository) {
        this.policyInstanceRepository = policyInstanceRepository;
    }


}
