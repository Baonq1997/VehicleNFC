package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PolicyHasVehicleType {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("vehicleTypeId")
    @Expose
    private VehicleType vehicleType;

    @SerializedName("pricingList")
    @Expose
    private List<Pricing> pricings;

    @SerializedName("minHour")
    @Expose
    private int minHour;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public List<Pricing> getPricings() {
        return pricings;
    }

    public void setPricings(List<Pricing> pricings) {
        this.pricings = pricings;
    }

    public int getMinHour() {
        return minHour;
    }

    public void setMinHour(int minHour) {
        this.minHour = minHour;
    }
}
