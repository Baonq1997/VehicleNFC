package com.example.demo.controller;

import com.example.demo.entity.VehicleType;
import com.example.demo.service.VehicleTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/vehicle-type")
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;

    public VehicleTypeController(VehicleTypeService vehicleTypeService) {
        this.vehicleTypeService = vehicleTypeService;
    }

    @GetMapping(value = {"/{id}"})
    public ResponseEntity<Optional<VehicleType>> getVehicleTypeById(@PathVariable("id") Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(vehicleTypeService.getVehicleTypeById(id));
    }

    @GetMapping(value = "/get-all")
    public ResponseEntity<List<VehicleType>> getAllVehicleTypes() {
        return ResponseEntity.status(HttpStatus.OK).body(vehicleTypeService.getAllVehicleTypes());
    }

}
