package com.example.demo.service;

import com.example.demo.model.VehicleType;
import com.example.demo.repository.VehicleTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleTypeService {
    private final VehicleTypeRepository vehicleTypeRepository;

    public VehicleTypeService(VehicleTypeRepository vehicleTypeRepository) {
        this.vehicleTypeRepository = vehicleTypeRepository;
    }

    public Optional<VehicleType> getVehicleTypeById(Integer id){
        return vehicleTypeRepository.findById(id);
    }

    public List<VehicleType> getAllVehicleTypes() {
        return vehicleTypeRepository.findAll();
    }
}
