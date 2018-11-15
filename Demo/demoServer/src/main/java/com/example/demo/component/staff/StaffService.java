package com.example.demo.component.staff;

import com.example.demo.component.order.RefundStatus;
import com.example.demo.component.order.RefundStatusRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffService {
    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Optional<Staff> getLoginStaff(String username, String password){
        return staffRepository.findByUsernameAndPassword(username,password);
    }
}
