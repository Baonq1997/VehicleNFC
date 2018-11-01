package com.example.demo.component.policy;

import com.example.demo.component.policy.Pricing;
import com.example.demo.component.policy.PricingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pricing")
public class PricingController {
    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping(value = "/get-by-policyAndVehicleType/{id}")
    public ResponseEntity<?> getPricingByPolicyAndVehicleType(@PathVariable("id") Integer id) {
        //Todo
        return null;
//        try{
//            return ResponseEntity.status(HttpStatus.OK).body(pricingService.findByPolicyHasVehicleTypeId(id));
//        }catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found price");
//        }
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity getPricing(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(pricingService.findById(id));
    }

    @PostMapping(value = "/save-pricing-json")
    public ResponseEntity savePricing(@RequestBody  Pricing pricing) {
        return ResponseEntity.status(HttpStatus.OK).body(pricingService.save(pricing));
    }

    @PostMapping(value = "/save-pricing")
    public ResponseEntity savePricingByForm(Pricing pricing) {
        return ResponseEntity.status(HttpStatus.OK).body(pricingService.save(pricing));
    }

    @PostMapping(value = "/delete-pricing/{id}")
    public String deletePricing(@PathVariable("id") Integer id) {
        pricingService.deletePricing(id);
        return "Success";
    }
}
