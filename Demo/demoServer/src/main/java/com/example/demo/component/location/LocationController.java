package com.example.demo.component.location;

import com.example.demo.config.PaginationEnum;
import com.example.demo.config.ResponseObject;
import com.example.demo.config.SearchCriteria;
import com.example.demo.view.AddLocationObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "/location")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping(value = {"get/{id}"})
    public ResponseEntity<?> getLocationBy(@PathVariable("id") Integer id) {
        try {
            System.out.println("Getting location info...");
            return ResponseEntity.status(HttpStatus.OK).body(locationService.getMeterById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found Location");
        }

    }

    @GetMapping("/index")
    public ModelAndView getAllMeters(ModelAndView mav) {
        mav.setViewName("location-management");
        return mav;
    }

    @GetMapping("/get-locations")
    public ResponseEntity<ResponseObject> getLocations() {
        ResponseObject response = locationService.getAllLocations();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/location-has-vehicles/{id}")
    public ResponseEntity getLocationHasVehicles(@PathVariable("id") Integer locationId) {
        return ResponseEntity.status(HttpStatus.OK).body(locationService.getLocationHasVehicleTypes(locationId));
    }

    @GetMapping("/policies/{id}")
    public ModelAndView getPoliciesOfLocation(@PathVariable("id") Integer id, ModelAndView mav) {
        mav.setViewName("location-policies");
        return mav;
    }

    @PostMapping("/add-policy")
    public ResponseEntity addPolicyToLocation(@RequestBody AddLocationObject addLocationObject) {
        locationService.addPolicy(addLocationObject);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/locations")
    public ResponseEntity getLocationsByPolicy(@RequestParam("policyId") Integer policyId) {
        return ResponseEntity.status(HttpStatus.OK).body(locationService.getLocationsByPolicyId(policyId));
    }

    @GetMapping("/create-policy")
    public ModelAndView createPolicy(@RequestParam(value = "locationId") Integer id,
                                     ModelAndView mav) {
        mav.setViewName("policy-create");
        return mav;
    }

    @PostMapping("/save")
    public ResponseEntity saveLocation(@RequestBody Location location) {
        boolean isExisted = locationService.checkExistedLocation(location.getLocation());
        if (isExisted) {
            return ResponseEntity.status(409).body("This location existed");
        }
        return ResponseEntity.status(HttpStatus.OK).body(locationService.saveLocation(location));
    }

    @GetMapping("/create")
    public ModelAndView createLocation(ModelAndView mav){
        mav.setViewName("create-location");
        return mav;
    }

    @PostMapping("/filter")
    public ResponseEntity filterLocation(@RequestBody List<SearchCriteria> searchCriteria
                                        ,@RequestParam("page") Integer pageNumber) {
        return ResponseEntity.status(HttpStatus.OK).body(locationService.filterLocation(searchCriteria,pageNumber, PaginationEnum.locationPageSize.getNumberOfRows()));
    }

    @PostMapping("/delete")
    public ResponseEntity deleteLocation(@RequestParam("id") Integer locationId) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete Successfully");
    }
}
