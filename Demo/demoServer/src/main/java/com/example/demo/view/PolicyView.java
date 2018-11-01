package com.example.demo.view;

import com.example.demo.component.policy.Policy;
import com.example.demo.component.vehicleType.VehicleType;

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
