package com.example.demo.entity;

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

    @JoinColumn(name = "tbl_vehicle_type_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private VehicleType vehicleTypeId;

    @Basic(optional = false)
    @Column(name = "tbl_policy_id", nullable = false)
    private Integer policyId;

    public PolicyHasTblVehicleType() {
    }

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

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }
}
