package com.example.demo.component.policy;

import com.example.demo.component.vehicleType.VehicleType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_policy_has_tbl_vehicle_type")
public class PolicyHasTblVehicleType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @Column(name = "min_hour")
    private Integer minHour;
    @Transient
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "policyInstanceHasTblVehicleTypeId")
    private List<Pricing> pricingList;

//    @Basic(optional = false)
    @Column(name = "tbl_policy_id", nullable = false)
    private Integer policyId;
    @JoinColumn(name = "tbl_vehicle_type_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private VehicleType vehicleTypeId;

    public PolicyHasTblVehicleType() {
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

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    public VehicleType getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(VehicleType vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }
}
