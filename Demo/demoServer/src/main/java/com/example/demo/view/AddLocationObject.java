package com.example.demo.view;

import com.example.demo.component.location.Location;

import java.io.Serializable;
import java.util.List;

public class AddLocationObject implements Serializable {
    private Integer policyId;
    private List<Location> locationArr;

    public AddLocationObject() {
    }

    public AddLocationObject(Integer policyId, List<Location> locationArr) {
        this.policyId = policyId;
        this.locationArr = locationArr;
    }

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    public List<Location> getLocationArr() {
        return locationArr;
    }

    public void setLocationArr(List<Location> locationArr) {
        this.locationArr = locationArr;
    }
}
