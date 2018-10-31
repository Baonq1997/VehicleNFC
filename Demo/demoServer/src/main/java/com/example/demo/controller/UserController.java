package com.example.demo.controller;

import com.example.demo.Config.PaginationEnum;
import com.example.demo.Config.ResponseObject;
import com.example.demo.Config.SearchCriteria;
import com.example.demo.entity.User;
import com.example.demo.entity.Vehicle;
import com.example.demo.service.PushNotificationService;
import com.example.demo.service.UserService;
import com.example.demo.service.VehicleService;
import com.example.demo.service.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private ServletContext servletContext;

    private final UserService userService;
    private final VehicleService vehicleService;
    private final VehicleTypeService vehicleTypeService;

    public UserController(UserService userService, VehicleService vehicleService, VehicleTypeService vehicleTypeService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.vehicleTypeService = vehicleTypeService;
    }

    @GetMapping(value = {"/get-user/{id}"})
    public ResponseEntity<Optional<User>> getUserById(@PathVariable("id") Integer id) {
        System.out.println("getting user info...");
        return status(OK).body(userService.getUserById(id));
    }

    @PostMapping(value = "/create-user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String hashedID = "";
        // Cần đoạn lấy thông tin xe
        userService.createUser(user);
//        }
        Optional<User> userOptional = userService.getUserByPhone(user.getPhoneNumber());
        if (userOptional.isPresent()) {
            User userDB = userOptional.get();
            int userId = userDB.getId();
            hashedID = userId + "";
//            hashedID = userService.hashID(userId);
        }
        return status(OK).body(hashedID);
    }


    @PostMapping(value = "/request-new-confirm")
    public ResponseEntity<Boolean> requestNewConfirm(@Param("phone") String phone) {
        // Cần đoạn lấy thông tin xe
        String confirmCode = encodeGenerator();
        System.err.println(phone + ", Code:" + confirmCode);
        userService.requestNewConfirmCode(phone, confirmCode);
        Map<String, String> confirmSMSList = (Map<String, String>) servletContext.getAttribute("confirmSMSList");
        if (confirmSMSList == null) {
            confirmSMSList = new HashMap<>();
        }
        confirmSMSList.put(phone, confirmCode);
        servletContext.setAttribute("confirmSMSList", confirmSMSList);
        return status(OK).body(true);
    }

    @PostMapping(value = "/confirm-user")
    public ResponseEntity<Boolean> confirmUser(@Param("phone") String phone, @Param("confirmCode") String confirmCode) {
        // Cần đoạn lấy thông tin xe
        Map<String, String> confirmSMSList = (Map<String, String>) servletContext.getAttribute("confirmSMSList");
//        String confirmCode = (String) session.getAttribute(user.getPhoneNumber());
        if (confirmSMSList != null) {
            String confirmCodeInSession = confirmSMSList.get(phone);
            if (confirmCodeInSession != null && confirmCodeInSession.equals(confirmCode)) {
                userService.activateUser(phone);
                return status(OK).body(true);
            }
        }
        return status(OK).body(false);
    }

    @PostMapping(value = "/confirm-reset-password")
    public ResponseEntity<Boolean> confirmResetPass(@Param("phone") String phone, @Param("confirmCode") String confirmCode) {
        // Cần đoạn lấy thông tin xe
        Map<String, String> confirmSMSList = (Map<String, String>) servletContext.getAttribute("confirmResetSMSList");
//        String confirmCode = (String) session.getAttribute(user.getPhoneNumber());
        if (confirmSMSList != null) {
            String confirmCodeInSession = confirmSMSList.get(phone);
            if (confirmCodeInSession != null && confirmCodeInSession.equals(confirmCode)) {
                userService.activateUser(phone);
                return status(OK).body(true);
            }
        }
        return status(OK).body(false);
    }

    @PostMapping("/save-user")
    public String updateUser(User user) {
        userService.updateUser(user);
        return "Success";
    }

    @PostMapping("/update-user-sms")
    public String updateUserSms(@RequestBody User user) {
        userService.updateUserSmsNoti(user);
        return "Success";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@Param(value = "id") Integer id) {
        userService.deleteUser(id);
        return "Success";
    }

    //    JsonPagination
    @GetMapping("/get-users-json")
    public ResponseEntity<ResponseObject> getUsers(@RequestParam(defaultValue = "0") Integer page) {
//        return ResponseEntity.status(OK).body(userService.getAllUser(page, PaginationEnum.userPageSize.getNumberOfRows()));
        List<User> listUser = userService.getUsers(page, PaginationEnum.userPageSize.getNumberOfRows());
        ResponseObject response = new ResponseObject();
        response.setData(listUser);
        response.setPageNumber(page);
        response.setTotalPages(userService.getTotalUsers(PaginationEnum.userPageSize.getNumberOfRows()).intValue());
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/get-user")
    public ModelAndView home(ModelAndView mav) {
        mav.setViewName("index");
        return mav;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Optional<User>> create(@Param("phone") String phone, @Param("password") String password) {
        Optional<User> result = userService.login(phone, password);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/search-user")
    public ResponseEntity<ResponseObject> searchUser(@RequestBody SearchCriteria params
            , @RequestParam(defaultValue = "0") Integer page) {
        ResponseObject response = new ResponseObject();
        response.setData(userService.searchUser(params, page, PaginationEnum.userPageSize.getNumberOfRows()));
        response.setPageNumber(page);
        response.setPageSize(PaginationEnum.userPageSize.getNumberOfRows());
        response.setTotalPages(userService.getTotalUsers(PaginationEnum.userPageSize.getNumberOfRows()).intValue());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/admin")
    public ModelAndView adminPage(ModelAndView mav) {
        mav.setViewName("admin");
        return mav;
    }


    @GetMapping("/vehicle")
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
                String confirmCode = encodeGenerator();
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

    public String encodeGenerator() {
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += (int) (Math.random() * (9)) + "";
        }
        return result;
    }


    @PostMapping(value = "/top-up")
    public ResponseEntity<Optional<User>> topUp(@Param("userId") String userId, @Param("amount") double amount) {
        return ResponseEntity.status(OK).body(userService.topUp(userId, amount));
    }

    @GetMapping(value = {"/get-user-by-phone"})
    public ResponseEntity<Optional<User>> getUserByPhoneNumber(@Param("phoneNumber") String phoneNumber) {
        System.out.println("getting user info...");
        return status(OK).body(userService.getUserByPhone(phoneNumber));
    }

    @GetMapping(value = {"/request-reset-password"})
    public ResponseEntity<Boolean> requestResetPassword(@Param("phoneNumber") String phoneNumber) {
        Optional<User> user = userService.getUserByPhone(phoneNumber);
        if (user.isPresent()) {
            String confirmCode = encodeGenerator();
            System.err.println(phoneNumber + ", Code: " + confirmCode);
            userService.requestNewConfirmCode(phoneNumber, confirmCode);
            Map<String, String> confirmSMSList = (Map<String, String>) servletContext.getAttribute("confirmResetSMSList");
            if (confirmSMSList == null) {
                confirmSMSList = new HashMap<>();
            }
            confirmSMSList.put(phoneNumber, confirmCode);
            servletContext.setAttribute("confirmResetSMSList", confirmSMSList);
            return status(OK).body(true);
        }
        return status(OK).body(false);
    }

    @PostMapping(value = "/change-password")
    public ResponseEntity<Boolean> changePassword(@Param("phoneNumber") String phoneNumber, @Param("oldPassword") String oldPassword,
                                                  @Param("newPassword") String newPassword) {
        return ResponseEntity.status(OK).body(userService.changePassword(phoneNumber, oldPassword, newPassword).isPresent());
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<Boolean> resetPassword(@Param("phoneNumber") String phoneNumber,
                                                 @Param("newPassword") String newPassword) {
        return ResponseEntity.status(OK).body(userService.resetPassword(phoneNumber, newPassword).isPresent());
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

    @GetMapping(value = "/register-token")
    public ResponseEntity<Boolean> registerToken(@RequestParam(value = "token") String token
            , @RequestParam(value = "PhoneNumber") String phoneNumber) {
        Map<String, String> registerTokenList = (Map<String, String>) servletContext.getAttribute("registerTokenList");
        if (registerTokenList == null) {
            registerTokenList = new HashMap<>();
        }
        registerTokenList.put(phoneNumber, token);
        System.err.println("Token: "+ phoneNumber+", " +token);
        servletContext.setAttribute("registerTokenList", registerTokenList);
        return ResponseEntity.status(OK).body(true);
    }

}
