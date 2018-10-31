package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vehicle {

    @SerializedName("vehicleNumber")
    @Expose
    private String vehicleNumber;

    @SerializedName("licensePlateId")
    @Expose
    private String licensePlateId;

    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("size")
    @Expose
    private String size;

    @SerializedName("verifyDate")
    @Expose
    private long verifyDate;

    @SerializedName("expireDate")
    @Expose
    private long expireDate;

    @SerializedName("vehicleTypeId")
    @Expose
    private VehicleType VehicleTypeId;

    @SerializedName("isVerified")
    @Expose
    private boolean isVerified;


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

    public long getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(long verifyDate) {
        this.verifyDate = verifyDate;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public VehicleType getVehicleTypeId() {
        return VehicleTypeId;
    }

    public void setVehicleTypeId(VehicleType vehicleTypeId) {
        VehicleTypeId = vehicleTypeId;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
