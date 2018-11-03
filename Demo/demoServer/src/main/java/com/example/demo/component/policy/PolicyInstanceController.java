package com.example.demo.component.policy;

import com.example.demo.component.vehicleType.VehicleType;
import com.example.demo.view.PolicyView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/policy-instance")
public class PolicyInstanceController {
    private PolicyInstanceService policyInstanceService;

    public PolicyInstanceController(PolicyInstanceService policyInstanceService) {
        this.policyInstanceService = policyInstanceService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity getById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(policyInstanceService.getById(id));
    }

    @PostMapping(value = "/create")
    public ResponseEntity createPolicy(@RequestBody PolicyView policyView) {
        try {
            Integer locationId = policyView.getLocationId();
            PolicyInstance policy = policyView.getPolicyInstance();
            List<VehicleType> vehicleTypeList = policyView.getVehicleTypes();

            return ResponseEntity.status(HttpStatus.OK).body(policyInstanceService.savePolicy(policy, vehicleTypeList, locationId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
