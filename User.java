package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Swomfire on 23-Sep-18.
 */

public class User {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("phoneNumber")
    @Expose
    private String phone;

    @SerializedName("money")
    @Expose
    private String money;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("vehicleNumber")
    @Expose
    private String vehicleNumber;

    @SerializedName("licensePlateId")
    @Expose
    private String licensePlateId;

    @SerializedName("vehicleTypeId")
    @Expose
    private VehicleType vehicleType;

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getMoney() {
        return money;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLicensePlateId() {
        return licensePlateId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public void setLicensePlateId(String licensePlateId) {
        this.licensePlateId = licensePlateId;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
