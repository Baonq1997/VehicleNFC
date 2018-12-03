package com.example.demo.component.policy;

import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/policy-vehicle")
public class PolicyHasVehicleController {
    private PolicyHasVehicleTypeService policyHasVehicleTypeService;

    public PolicyHasVehicleController(PolicyHasVehicleTypeService policyHasVehicleTypeService) {
        this.policyHasVehicleTypeService = policyHasVehicleTypeService;
    }

    @GetMapping("/policy-vehicles")
    public ResponseEntity<List<PolicyHasTblVehicleType>> getByPolicy(@RequestParam("policyId") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(policyHasVehicleTypeService.getByPolicyId(id));
    }

    @PostMapping(value = "/update-min-hour")
    public ResponseEntity saveMinHour(@Param("minHour") Integer minHour
            ,@Param("policyHasVehicleTypeId") Integer policyHasVehicleTypeId) {
        return ResponseEntity.status(HttpStatus.OK).body(policyHasVehicleTypeService.saveMinHour(minHour, policyHasVehicleTypeId));
    }

}
