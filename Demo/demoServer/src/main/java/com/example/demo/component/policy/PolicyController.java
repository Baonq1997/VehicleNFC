package com.example.demo.component.policy;

import com.example.demo.component.policy.Policy;
import com.example.demo.component.policy.PolicyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "/policy")
public class PolicyController {
    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }
    @GetMapping(value = {"/get/{id}"})
    public ResponseEntity<?> getPolicyById(@PathVariable("id") Integer id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(policyService.getPolicyById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Policy");
        }
    }

    @GetMapping("/edit")
    public ModelAndView index(ModelAndView mav
                            , @RequestParam("policyId") Integer policyId
                            , @RequestParam("locationId") Integer locaitonId) {
        mav.setViewName("policy-edit");
        return mav;
    }

    @GetMapping("/create")
    public ModelAndView createPage(ModelAndView mav) {
        mav.setViewName("policy-create");
        return mav;
    }

    @GetMapping("/index")
    public ModelAndView index(ModelAndView mav) {
        mav.setViewName("policies");
        return mav;
    }
    //Todo
//    @PostMapping(value = "/create")
//    public ResponseEntity createPolicy(@RequestBody PolicyView policyView) {
//        try {
//            Integer locationId = policyView.getLocationId();
//            Policy policy = policyView.getPolicy();
////            List<VehicleType> vehicleTypeList = policyView.getVehicleTypes();
//
////            return ResponseEntity.status(HttpStatus.OK).body(policyService.savePolicy(policy, vehicleTypeList, locationId));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }

    //Todo
//    @PostMapping(value = "/delete")
//    public ResponseEntity deletePolicy(@RequestBody DeletePolicyObject deletePolicyObject) {
//        try{
//            Integer locationId = deletePolicyObject.getLocationId();
//            Policy policy = deletePolicyObject.getPolicy();
//            List<Integer> policyHasVehicleTypeIdList = deletePolicyObject.getPolicyHasVehicleTypeId();
//            List<VehicleType> vehicleTypeList  = deletePolicyObject.getVehicleTypes();
//            policyService.deletePolicy(locationId, policy, policyHasVehicleTypeIdList);
//            return ResponseEntity.status(HttpStatus.OK).body("Success");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }

    @PostMapping(value = "/delete-by-location-policy")
    public ResponseEntity deleteByLocationIdAndId(@RequestParam("locationId") Integer locationId
                                                , @RequestParam("policyId") Integer policyId) {
        policyService.deleteByIdAndLocationId(locationId, policyId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/policies")
    public ResponseEntity getAllPolicies() {
       List<Policy> policyList =policyService.getAllPolicies();
       if (policyList.isEmpty()) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not load polices");
       } else {
           return ResponseEntity.status(HttpStatus.OK).body(policyList);
       }
    }


}
