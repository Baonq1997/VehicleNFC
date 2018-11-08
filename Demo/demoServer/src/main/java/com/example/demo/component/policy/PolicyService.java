package com.example.demo.component.policy;

import org.springframework.stereotype.Service;

@Service
public class PolicyService {
    private PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }


}
