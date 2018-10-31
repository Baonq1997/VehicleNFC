package com.example.demo.entity;


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_location")
public class Location implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "location")
    private String location;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @Column(name = "is_activated")
    private Boolean isActivated;

    @Transient
    private List<PolicyInstance> policyInstanceList;
    //    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblLocationId")
    @Transient
    private List<Order> orderList;
    @Transient
    private String isDelete;

    public Location() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
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

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public List<Order> getorderList() {
        return orderList;
    }

    public void setTblOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public List<PolicyInstance> getPolicyInstanceList() {
        return policyInstanceList;
    }

    public void setPolicyInstanceList(List<PolicyInstance> policyInstanceList) {
        this.policyInstanceList = policyInstanceList;
    }
}


