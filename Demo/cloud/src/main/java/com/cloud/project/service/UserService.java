package com.cloud.project.service;

import com.cloud.project.model.Gundam;
import com.cloud.project.model.User;
import com.cloud.project.repository.GundamRepository;
import com.cloud.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password).get();
    }

}
