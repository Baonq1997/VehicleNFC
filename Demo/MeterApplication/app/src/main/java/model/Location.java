package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Swomfire on 23-Sep-18.
 */

public class Location {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("isActivated")
    @Expose
    private boolean isActivated;


    @SerializedName("policyList")
    @Expose
    private List<Policy> policies;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }
}
