package com.example.demo.component.policy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_pricing")
public class Pricing implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "from_hour")
    private int fromHour;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price_per_hour")
    private double pricePerHour;
    @Column(name = "late_fee_per_hour")
    private double lateFeePerHour;
//    @ManyToMany(mappedBy = "pricingList")

//    @JoinColumn(name = "tbl_policy_has_tbl_vehicle_type_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
    @Column(name = "tbl_policy_has_tbl_vehicle_type_id")
    private Integer policyHasTblVehicleTypeId;
    public Pricing() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getFromHour() {
        return fromHour;
    }

    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public double getLateFeePerHour() {
        return lateFeePerHour;
    }

    public void setLateFeePerHour(double lateFeePerHour) {
        this.lateFeePerHour = lateFeePerHour;
    }

    public Integer getPolicyHasTblVehicleTypeId() {
        return policyHasTblVehicleTypeId;
    }

    public void setPolicyHasTblVehicleTypeId(Integer policyHasTblVehicleTypeId) {
        this.policyHasTblVehicleTypeId = policyHasTblVehicleTypeId;
    }
}
