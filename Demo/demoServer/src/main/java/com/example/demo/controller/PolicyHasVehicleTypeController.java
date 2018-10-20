package com.example.demo.controller;

import com.example.demo.entities.Policy;
import com.example.demo.entities.PolicyHasTblVehicleType;
import com.example.demo.service.PolicyHasVehicleTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sun.misc.Request;

import java.util.List;

@RestController
@RequestMapping("/policy-vehicleType")
public class PolicyHasVehicleTypeController {
    private final PolicyHasVehicleTypeService policyHasVehicleTypeService;

    public PolicyHasVehicleTypeController(PolicyHasVehicleTypeService policyHasVehicleTypeService) {
        this.policyHasVehicleTypeService = policyHasVehicleTypeService;
    }

    @GetMapping("/get-by-policy-vehicleType")
    public ResponseEntity<?> getByPolicyAndVehicleType
            (@RequestParam(value = "policyId") Integer policyID
            ,@RequestParam(value = "vehicleTypeId") Integer vehicleTypeId) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(policyHasVehicleTypeService.findByPolicyAndVehicleType(policyID,vehicleTypeId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found PolicyHasVehicleType");
        }
    }

    @GetMapping("/get-vehicleTypes/{id}")
    public ResponseEntity<?> getByPolicyID(@PathVariable("id") Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(policyHasVehicleTypeService.findByPolicyId(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found PolicyHasVehicleType");
        }
    }
    @PostMapping("/get-vehicleTypes-byPolicies")
    public ResponseEntity<?> getByListPolicy(@RequestBody List<Policy> policyList) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(policyHasVehicleTypeService.findByPolicyList(policyList));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not load PolicyHasVehicleType");
        }
    }

    @PostMapping("/save")
    public String save(@RequestBody PolicyHasTblVehicleType policyHasTblVehicleType) {
            policyHasVehicleTypeService.save(policyHasTblVehicleType);
            return "Success";
    }

    @PostMapping("/delete")
    public String delete(@RequestBody PolicyHasTblVehicleType policyHasTblVehicleType) {
        policyHasVehicleTypeService.delete(policyHasTblVehicleType);
        return "Success";
    }

    @GetMapping("/get-by-policy")
    public ResponseEntity getByPolicy(@RequestParam("policyId") Integer policyId) {
        List<PolicyHasTblVehicleType> policyHasTblVehicleTypes = policyHasVehicleTypeService.findListByPolicyId(policyId);
        if (policyHasTblVehicleTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(policyHasTblVehicleTypes);
    }

}
