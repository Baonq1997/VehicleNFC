package com.example.demo.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_policy_instance")
public class PolicyInstance implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "allowed_parking_from", nullable = false)
    private long allowedParkingFrom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "allowed_parking_to", nullable = false)
    private long allowedParkingTo;

    @Basic(optional = false)
    @NotNull
    @Column(name = "tbl_location_id", nullable = false)
    private Integer locationId;
    @JoinColumn(name = "tbl_policy_id", referencedColumnName = "id")
    @ManyToOne
    private Policy policyId;

    @Transient
    private List<PolicyInstanceHasTblVehicleType> policyInstanceHasTblVehicleTypes;

    public PolicyInstance() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Policy getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Policy policyId) {
        this.policyId = policyId;
    }

    public List<PolicyInstanceHasTblVehicleType> getPolicyInstanceHasTblVehicleTypes() {
        return policyInstanceHasTblVehicleTypes;
    }

    public void setPolicyInstanceHasTblVehicleTypes(List<PolicyInstanceHasTblVehicleType> policyInstanceHasTblVehicleTypes) {
        this.policyInstanceHasTblVehicleTypes = policyInstanceHasTblVehicleTypes;
    }
}
