package com.example.demo.component.policy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/policy-instance-vehicle")
public class PolicyInstanceHasVehicleController {
    private PolicyInstanceHasVehicleTypeService policyInstanceHasVehicleTypeService;

    public PolicyInstanceHasVehicleController(PolicyInstanceHasVehicleTypeService policyInstanceHasVehicleTypeService) {
        this.policyInstanceHasVehicleTypeService = policyInstanceHasVehicleTypeService;
    }

    @GetMapping("/policy-instance-vehicles")
    public ResponseEntity<List<PolicyInstanceHasTblVehicleType>> getByPolicyInstance(@RequestParam("policyInstanceId") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(policyInstanceHasVehicleTypeService.getByPolicyInstanceId(id));
    }
}
