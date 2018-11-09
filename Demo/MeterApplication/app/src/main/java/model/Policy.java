package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Policy {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("policyHasTblVehicleTypes")
    @Expose
    private List<PolicyHasVehicleType> policyHasVehicleTypes;

    @SerializedName("allowedParkingFrom")
    @Expose
    private long allowedParkingFrom;

    @SerializedName("allowedParkingTo")
    @Expose
    private long allowedParkingTo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<PolicyHasVehicleType> getPolicyHasVehicleTypes() {
        return policyHasVehicleTypes;
    }

    public void setPolicyHasVehicleTypes(List<PolicyHasVehicleType> policyHasVehicleTypes) {
        this.policyHasVehicleTypes = policyHasVehicleTypes;
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
