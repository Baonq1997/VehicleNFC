package com.example.demo.component.vehicle;

import com.example.demo.component.vehicleType.VehicleTypeService;
import com.example.demo.config.PaginationEnum;
import com.example.demo.config.ResponseObject;
import com.example.demo.config.SearchCriteria;
import com.example.demo.component.user.User;
import com.example.demo.service.PushNotificationService;
import com.example.demo.component.user.UserService;
import com.example.demo.service.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@CrossOrigin
@RequestMapping(value = "/vehicle")
public class VehicleController {

    @Autowired
    private ServletContext servletContext;

    private final UserService userService;
    private final VehicleService vehicleService;
    private final VehicleTypeService vehicleTypeService;

    public VehicleController(UserService userService, VehicleService vehicleService, VehicleTypeService vehicleTypeService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.vehicleTypeService = vehicleTypeService;
    }

    @GetMapping("/index")
    public ModelAndView vehiclePage(ModelAndView mav) {
        mav.setViewName("vehicle");
        return mav;
    }

    @GetMapping("/lookup-vehicle")
    public ModelAndView lookupPage(ModelAndView mav) {
        mav.setViewName("lookup-vehicle");
        return mav;
    }

    @PostMapping("/verify-vehicle")
    public Boolean verifyPage(Vehicle vehicle) {
        if (vehicleService.verifyVehicle(vehicle).isPresent()) {
            Optional<User> user = userService.getUserByVehicleNumber(vehicle.getVehicleNumber());
            if (user.isPresent()) {
                String confirmCode = UtilityService.encodeGenerator();
                PushNotificationService.sendPhoneConfirmNotification(null, user.get().getPhoneNumber(), confirmCode);
                System.err.println(user.get().getPhoneNumber() + ", Code:" + confirmCode);
                Map<String, String> confirmSMSList = (Map<String, String>) servletContext.getAttribute("confirmSMSList");
                if (confirmSMSList == null) {
                    confirmSMSList = new HashMap<>();
                }
                confirmSMSList.put(user.get().getPhoneNumber(), confirmCode);
                servletContext.setAttribute("confirmSMSList", confirmSMSList);
            }
            return true;
        }
        return false;
    }

    @PostMapping("/save-vehicle")
    public Boolean savePage(Vehicle vehicle) {
        if (vehicleService.saveVehicle(vehicle).isPresent()) {
            return true;
        }
        return false;
    }


    @GetMapping("/verify-vehicle-form")
    public ModelAndView verifyForm(ModelAndView mav) {
        mav.setViewName("verify-vehicle-form");
        mav.addObject("vehicleNumber", "abc");
        return mav;
    }

    @GetMapping(value = "/get-vehicles")
    public ResponseEntity<ResponseObject> getVehicles(@RequestParam(defaultValue = "0") Integer page) {
        ResponseObject response = new ResponseObject();
        response.setData(vehicleService.getAllVehicle(page, PaginationEnum.userPageSize.getNumberOfRows()));
        response.setPageNumber(page);
        response.setTotalPages(vehicleService.getTotalVehicles(PaginationEnum.userPageSize.getNumberOfRows()).intValue());

        return ResponseEntity.status(OK).body(response);
    }

    @PostMapping("/search-vehicle")
    public ResponseEntity<ResponseObject> searchVehicle(@RequestBody SearchCriteria params
            , @RequestParam(defaultValue = "0") Integer page) {
        ResponseObject response = new ResponseObject();
//        response.setPageNumber(page);
//        response.setPageSize(PaginationEnum.userPageSize.getNumberOfRows());
//        response.setTotalPages(vehicleService.getTotalVehicles(PaginationEnum.userPageSize.getNumberOfRows()).intValue());
        response.setData(vehicleService.searchVehicle(params, page, PaginationEnum.userPageSize.getNumberOfRows()));

        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping(value = "/get-vehicle/{vehicleNumber}")
    public ResponseEntity<Optional<Vehicle>> getVehicle(@PathVariable(value = "vehicleNumber") String vehicleNumber) {
        return ResponseEntity.status(OK).body(vehicleService.getVehicle(vehicleNumber));
    }

    @PostMapping(value = "/delete-vehicle")
    public ResponseEntity<Boolean> deleteVehicle(@Param(value = "vehicleNumber") String vehicleNumber) {
        return ResponseEntity.status(OK).body(vehicleService.deleteVehicle(vehicleNumber));
    }
}
