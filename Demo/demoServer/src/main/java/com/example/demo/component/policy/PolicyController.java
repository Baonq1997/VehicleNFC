package com.example.demo.component.policy;

import com.example.demo.component.vehicleType.VehicleType;
import com.example.demo.config.PaginationEnum;
import com.example.demo.config.SearchCriteria;
import com.example.demo.view.DeletePolicyObject;
import com.example.demo.view.PolicyView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "/policy")
public class PolicyController {
    private PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping("/policies")
    public ResponseEntity getPolicies() {
        return ResponseEntity.status(HttpStatus.OK).body(policyService.getAll());
    }

    @GetMapping("/locations-policies")
    public ResponseEntity getPolicy(@RequestParam("locationId") Integer locationId) {
        return ResponseEntity.status(HttpStatus.OK).body(policyService.getByLocationId(locationId));
    }

    @PostMapping("/filter-policies")
    public ResponseEntity filterPoliciesByLocation(@RequestBody List<SearchCriteria> params
            ,@RequestParam(value = "locationId", defaultValue = "0") Integer locationId
            ,@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.status(HttpStatus.OK).body(policyService.filterPoliciesByLocation(locationId, params, page, PaginationEnum.userPageSize.getNumberOfRows()));
    }

    @GetMapping("/edit")
    public ModelAndView index(ModelAndView mav
            , @RequestParam("policyId") Integer policyId) {
        mav.setViewName("policy-edit");
        return mav;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity getById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(policyService.getById(id));
    }

    @PostMapping(value = "/create")
    public ResponseEntity createPolicy(@RequestBody PolicyView policyView) {
        try {
            Integer locationId = policyView.getLocationId();
            Policy policy = policyView.getPolicy();
            List<VehicleType> vehicleTypeList = policyView.getVehicleTypes();
            if (policyView.getPolicy().getId() == null) {
                if (policyService.isExistedPolicy(policy, locationId)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Existed Policy");
                }
            }

            return ResponseEntity.status(HttpStatus.OK).body(policyService.savePolicy(policy, vehicleTypeList, locationId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error has occurred");
        }
    }


    @GetMapping(value = "/create")
    public ModelAndView createPolicy(ModelAndView mav) {
        mav.setViewName("create-policy");
        return mav;
    }

    @PostMapping(value = "/delete-by-location-policy")
    public ResponseEntity deleteByLocationIdAndId( @RequestParam("policyId") Integer policyId) {
        policyService.deleteBylocationIdAndPolicyId(policyId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/delete-policy")
    public ResponseEntity deleteById(@RequestParam("policyId") Integer policyId) {
        policyService.deleteBylocationIdAndPolicyId(policyId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/delete")
    public ResponseEntity deletePolicy(@RequestBody DeletePolicyObject deletePolicyObject) {
        try{
            Integer locationId = deletePolicyObject.getLocationId();
            Policy policy = deletePolicyObject.getPolicy();
            List<Integer> policyHasVehicleTypeIdList = deletePolicyObject.getPolicyHasVehicleTypeId();
            List<VehicleType> vehicleTypeList  = deletePolicyObject.getVehicleTypes();
            policyService.deletePolicy(locationId, policy, policyHasVehicleTypeIdList);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @GetMapping("/index")
    public ModelAndView index(ModelAndView mav) {
        mav.setViewName("policies");
        return mav;
    }

    @GetMapping("/add-policy")
    public ModelAndView addPolicy(ModelAndView mav) {
        mav.setViewName("policy-add-location");
        return mav;
    }
}
