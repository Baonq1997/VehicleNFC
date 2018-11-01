package com.example.demo.component.policy;

import com.example.demo.component.vehicleType.VehicleType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_policy")
public class Policy implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "allowed_parking_from")
    private long allowedParkingFrom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "allowed_parking_to")
    private long allowedParkingTo;
    @JoinTable(name = "tbl_policy_has_tbl_vehicle_type", joinColumns = {
            @JoinColumn(name = "tbl_policy_id", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "tbl_vehicle_type_id", referencedColumnName = "id")})
    @ManyToMany
    private List<VehicleType> vehicleTypeList;
    @OneToMany(mappedBy = "policyId")
    private List<PolicyInstance> policyInstanceList;

    public Policy() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getAllowedParkingFrom() {
        return allowedParkingFrom;
    }

    public void setAllowedParkingFrom(long allowedParkingFrom) {
        this.allowedParkingFrom = allowedParkingFrom;
    }

    public long getAllowedParkingTo() {
        return allowedParkingTo;
    }

    public void setAllowedParkingTo(long allowedParkingTo) {
        this.allowedParkingTo = allowedParkingTo;
    }

    public List<VehicleType> getVehicleTypeList() {
        return vehicleTypeList;
    }

    public void setVehicleTypeList(List<VehicleType> vehicleTypeList) {
        this.vehicleTypeList = vehicleTypeList;
    }
}
