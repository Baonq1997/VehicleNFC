package com.example.demo.component.user;

import com.example.demo.config.PaginationEnum;
import com.example.demo.config.ResponseObject;
import com.example.demo.config.SearchCriteria;
import com.example.demo.service.UtilityService;
import com.example.demo.component.vehicle.VehicleService;
import com.example.demo.component.vehicleType.VehicleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.util.*;

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
    public ResponseEntity<Optional<User>> getUserById(@PathVariable("id") String id) {
        System.out.println("getting user info...");
        return status(OK).body(userService.getUserById(UserService.decodeId(id)));
    }

    @PostMapping(value = "/create-user")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String encodedId = UserService.encodeId(userService.createUser(user));
        return status(OK).body(encodedId);
    }


    @PostMapping(value = "/request-new-confirm")
    public ResponseEntity<Boolean> requestNewConfirm(@Param("phone") String phone) {
        // Cần đoạn lấy thông tin xe
        String confirmCode = UtilityService.encodeGenerator();
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

    @PostMapping("/update-user")
    public ResponseEntity<Boolean> updateUser(@RequestBody User user) {
        user.setId(UserService.decodeId(user.getDecodedId()));
        return status(OK).body(userService.updateUser(user));
    }

    @PostMapping("/update-user-sms")
    public ResponseEntity<Optional<User>> updateUserSms(@RequestBody User user) {
        return status(OK).body(userService.updateUserSmsNoti(user));
    }

    @PostMapping("/delete-user")
    public String deleteUser(@Param(value = "id") String id) {
        try {
            userService.deleteUser(UserService.decodeId(id));
        } catch (Exception e) {
        }
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

    @GetMapping("/get-verify")
    public ModelAndView verify(ModelAndView mav) {
        mav.setViewName("user-verify");
        return mav;
    }

    @GetMapping("/get-create")
    public ModelAndView create(ModelAndView mav) {
        mav.setViewName("user-create");
        return mav;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Optional<User>> login(@Param("phone") String phone, @Param("password") String password) {
        Optional<User> result = userService.login(phone, password);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/search-user")
    public ResponseEntity<ResponseObject> searchUser(@RequestBody List<SearchCriteria> params
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

    @PostMapping(value = "/top-up")
    public ResponseEntity<Optional<User>> topUp(@Param("userId") String userId, @Param("amount") double amount) {
        return ResponseEntity.status(OK).body(userService.topUp(UserService.decodeId(userId), amount));
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
            String confirmCode = UtilityService.encodeGenerator();
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


    @GetMapping(value = "/register-token")
    public ResponseEntity<Boolean> registerToken(@RequestParam(value = "token") String token
            , @RequestParam(value = "PhoneNumber") String phoneNumber) {
        Map<String, String> registerTokenList = (Map<String, String>) servletContext.getAttribute("registerTokenList");
        if (registerTokenList == null) {
            registerTokenList = new HashMap<>();
        }
        registerTokenList.put(phoneNumber, token);
        System.err.println("Token: " + phoneNumber + ", " + token);
        servletContext.setAttribute("registerTokenList", registerTokenList);
        return ResponseEntity.status(OK).body(true);
    }

    @PostMapping(value = "/unbind-vehicle")
    public ResponseEntity<Boolean> unbindVehicle(@Param("userId") String userId) {
        return ResponseEntity.status(OK).body(userService.unbindVehicle(UserService.decodeId(userId)));
    }

}
