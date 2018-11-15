package com.example.demo.component.staff;

import com.example.demo.component.policy.Pricing;
import com.example.demo.component.policy.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/staff")
public class StaffController {

    @Autowired
    private ServletContext servletContext;

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(ModelAndView mav) {
        mav.setViewName("login");
        return mav;
    }

    @GetMapping("/admin")
    public ModelAndView adminPage(ModelAndView mav) {
        mav.setViewName("admin");
        return mav;
    }

    @GetMapping("/manager")
    public ModelAndView managerPage(ModelAndView mav) {
        mav.setViewName("manager");
        return mav;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Staff> login(@Param("username") String username, @Param("password") String password) {
        Optional<Staff> staff =staffService.getLoginStaff(username, password);
        if (staff.isPresent()){
            if (staff.get().getActive()) {
                return ResponseEntity.status(HttpStatus.OK).body(staff.get());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(value = "/token")
    public ResponseEntity<Boolean> tokenRegister(@RequestParam("username") String username, @RequestParam("token") String token
    , @RequestParam("isManager") boolean isManager){
        Map<String, String> tokenList;
        if (isManager){
            tokenList = (Map<String, String>) servletContext.getAttribute("managerTokenList");
        }else {
            tokenList = (Map<String, String>) servletContext.getAttribute("staffTokenList");
        }
        if (tokenList == null){
            tokenList = new HashMap<>();
        }
        tokenList.put(username,token);
        if (isManager){
            servletContext.setAttribute("managerTokenList",tokenList);
        }else {
            servletContext.setAttribute("staffTokenList",tokenList);
        }
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

}
