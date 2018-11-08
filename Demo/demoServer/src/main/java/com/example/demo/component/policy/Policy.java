package com.example.demo.component.policy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_policy")
public class Policy implements Serializable {
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

    @JoinColumn(name = "tbl_policy_id", nullable = false, insertable = true,
            updatable = false)
    @OneToMany
//    @Transient
    private List<PolicyHasTblVehicleType> policyHasTblVehicleTypes;


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

    public List<PolicyHasTblVehicleType> getPolicyHasTblVehicleTypes() {
        return policyHasTblVehicleTypes;
    }

    public void setPolicyHasTblVehicleTypes(List<PolicyHasTblVehicleType> policyHasTblVehicleTypes) {
        this.policyHasTblVehicleTypes = policyHasTblVehicleTypes;
    }
}
