package com.example.demo.view;

import com.example.demo.entities.Location;
import com.example.demo.entities.Policy;
import com.example.demo.entities.VehicleType;

import java.io.Serializable;
import java.util.List;

public class PolicyView implements Serializable {
    private Integer locationId;
    private Policy policy;
    private List<VehicleType> vehicleTypes;

    public PolicyView() {
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public List<VehicleType> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<VehicleType> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }
}
