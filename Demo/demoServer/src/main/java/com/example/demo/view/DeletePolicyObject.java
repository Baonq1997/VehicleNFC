package com.example.demo.view;


import com.example.demo.entity.Policy;
import com.example.demo.entity.VehicleType;

import java.io.Serializable;
import java.util.List;

public class DeletePolicyObject implements Serializable {
    private Integer locationId;
    private Policy policy;
    private List<Integer> policyHasVehicleTypeId;
    private List<VehicleType> vehicleTypes;

    public DeletePolicyObject() {
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

    public List<Integer> getPolicyHasVehicleTypeId() {
        return policyHasVehicleTypeId;
    }

    public void setPolicyHasVehicleTypeId(List<Integer> policyHasVehicleTypeId) {
        this.policyHasVehicleTypeId = policyHasVehicleTypeId;
    }

    public List<VehicleType> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<VehicleType> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }

}
