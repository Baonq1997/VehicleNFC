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
    @Column(name = "id", nullable = false)
    private Integer id;

    @JoinColumn(name = "tbl_vehicle_type_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private VehicleType vehicleTypeId;

    @Basic(optional = false)
    @Column(name = "tbl_location_id", nullable = false)
    private Integer locationId;

    @Basic(optional = false)
    @Column(name = "min_hour", nullable = false)
    private Integer minHour;

    @JoinColumn(name = "tbl_policy_id")
    @OneToMany
    private List<Pricing> pricings;

    @Basic(optional = false)
    @NotNull
    @Column(name = "allowed_parking_from")
    private long allowedParkingFrom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "allowed_parking_to")
    private long allowedParkingTo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VehicleType getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(VehicleType vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getMinHour() {
        return minHour;
    }

    public void setMinHour(Integer minHour) {
        this.minHour = minHour;
    }

    public List<Pricing> getPricings() {
        return pricings;
    }

    public void setPricings(List<Pricing> pricings) {
        this.pricings = pricings;
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
}
