package com.cloud.project.controller;

import com.cloud.project.model.Gundam;
import com.cloud.project.model.User;
import com.cloud.project.service.GundamService;
import com.cloud.project.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    //    private final GundamService gundamService;
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public User login(@Param(value = "username") String username, @Param(value = "password") String password, HttpServletRequest request) {
        User user = userService.login(username, password);
        if (user != null) {
        HttpSession session = request.getSession();
        session.setAttribute(username,user.getName());
        }
        return user;
    }


}
