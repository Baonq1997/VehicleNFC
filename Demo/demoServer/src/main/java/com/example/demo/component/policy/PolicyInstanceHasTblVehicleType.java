package com.example.demo.component.policy;

import com.example.demo.component.vehicleType.VehicleType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_policy_instance_has_tbl_vehicle_type")
public class PolicyInstanceHasTblVehicleType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "min_hour")
    private Integer minHour;
    @JoinTable(name = "tbl_policy_instance_has_tbl_vehicle_type_has_tbl_pricing", joinColumns = {
            @JoinColumn(name = "tbl_policy_instance_has_tbl_vehicle_type_id", referencedColumnName = "id", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "tbl_pricing_id", referencedColumnName = "id", nullable = false)})
    @ManyToMany
    private List<Pricing> pricingList;

    @Basic(optional = false)
    @Column(name = "tbl_policy_instance_id", nullable = false)
    private Integer policyInstanceId;
    @JoinColumn(name = "tbl_vehicle_type_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private VehicleType vehicleTypeId;

    public PolicyInstanceHasTblVehicleType() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMinHour() {
        return minHour;
    }

    public void setMinHour(Integer minHour) {
        this.minHour = minHour;
    }

    public List<Pricing> getPricingList() {
        return pricingList;
    }

    public void setPricingList(List<Pricing> pricingList) {
        this.pricingList = pricingList;
    }

    public Integer getPolicyInstanceId() {
        return policyInstanceId;
    }

    public void setPolicyInstanceId(Integer policyInstanceId) {
        this.policyInstanceId = policyInstanceId;
    }

    public VehicleType getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(VehicleType vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }
}
