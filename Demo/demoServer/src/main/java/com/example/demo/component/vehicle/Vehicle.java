package com.example.demo.component.vehicle;

import com.example.demo.component.vehicleType.VehicleType;
import com.example.demo.component.user.User;
import com.example.demo.view.Owner;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tbl_vehicle")
public class Vehicle {
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "vehicle_number")
    private String vehicleNumber;
    @Size(max = 10)
    @Column(name = "license_plate_id")
    private String licensePlateId;
    @Size(max = 45)
    @Column(name = "brand")
    private String brand;
    @Size(max = 255)
    @Column(name = "size")
    private String size;
    @Column(name = "expire_date")
    private Long expireDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "is_verified")
    private boolean isVerified;

    @JoinColumn(name = "tbl_vehicle_type_id", referencedColumnName = "id")
    @ManyToOne
    private VehicleType vehicleTypeId;

    @Transient
    private Owner owner;

    public Vehicle() {
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getLicensePlateId() {
        return licensePlateId;
    }

    public void setLicensePlateId(String licensePlateId) {
        this.licensePlateId = licensePlateId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public VehicleType getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(VehicleType vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}
