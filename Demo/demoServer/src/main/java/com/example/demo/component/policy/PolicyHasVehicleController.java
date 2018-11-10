package com.example.demo.component.policy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/policy-instance-vehicle")
public class PolicyHasVehicleController {
    private PolicyHasVehicleTypeService policyHasVehicleTypeService;

    public PolicyHasVehicleController(PolicyHasVehicleTypeService policyHasVehicleTypeService) {
        this.policyHasVehicleTypeService = policyHasVehicleTypeService;
    }

    @GetMapping("/policy-instance-vehicles")
    public ResponseEntity<List<PolicyHasTblVehicleType>> getByPolicyInstance(@RequestParam("policyInstanceId") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(policyHasVehicleTypeService.getByPolicyInstanceId(id));
    }
}
