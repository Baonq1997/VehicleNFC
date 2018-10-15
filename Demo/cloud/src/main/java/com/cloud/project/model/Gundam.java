package com.cloud.project.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "gundam")
public class Gundam implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
//    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "img_src")
    private String image;

    @Column(name = "price")
    private Double price;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private int quantity;

    @JoinColumn(name = "manufacture_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private Manufacture manufacture;

    @JoinColumn(name = "gundam_status_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private GundamStatus gundamStatus;

    public Gundam() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Manufacture getManufacture() {
        return manufacture;
    }

    public void setManufacture(Manufacture manufacture) {
        this.manufacture = manufacture;
    }

    public GundamStatus getGundamStatus() {
        return gundamStatus;
    }

    public void setGundamStatus(GundamStatus gundamStatus) {
        this.gundamStatus = gundamStatus;
    }
}
