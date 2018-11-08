package com.example.demo.component.order;

import com.example.demo.component.user.User;
import com.example.demo.component.vehicleType.VehicleType;
import com.example.demo.component.location.Location;
import com.example.demo.model.HourHasPrice;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tbl_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "total")
    private Double total;
    @Basic(optional = false)
    @NotNull
    @Column(name = "check_in_date")
    private long checkInDate;
    @Column(name = "check_out_date")
    private Long checkOutDate;
    @Column(name = "duration")
    private Long duration;
    @Column(name = "allowed_parking_from")
    private Long allowedParkingFrom;
    @Column(name = "allowed_parking_to")
    private Long allowedParkingTo;
    @Column(name = "min_hour")
    private Integer minHour;
    @JoinColumn(name = "tbl_order_status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private OrderStatus orderStatusId;
    @JoinColumn(name = "tbl_user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private User userId;
    @Column(name = "tbl_location_id")
    private Integer locationId;
    @JoinColumn(name = "tbl_vehicle_type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private VehicleType vehicleTypeId;
    //    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tblOrderId")
    @OneToMany(mappedBy = "orderId")
    private List<OrderPricing> orderPricingList;
    @Transient
    private List<HourHasPrice> hourHasPrices;

    @Transient
    private Location location;

    public Order() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public long getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(long checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Long getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Long checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getAllowedParkingFrom() {
        return allowedParkingFrom;
    }

    public void setAllowedParkingFrom(Long allowedParkingFrom) {
        this.allowedParkingFrom = allowedParkingFrom;
    }

    public Long getAllowedParkingTo() {
        return allowedParkingTo;
    }

    public void setAllowedParkingTo(Long allowedParkingTo) {
        this.allowedParkingTo = allowedParkingTo;
    }

    public Integer getMinHour() {
        return minHour;
    }

    public void setMinHour(Integer minHour) {
        this.minHour = minHour;
    }

    public OrderStatus getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(OrderStatus orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public VehicleType getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(VehicleType vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public List<OrderPricing> getOrderPricingList() {
        return orderPricingList;
    }

    public void setOrderPricingList(List<OrderPricing> orderPricingList) {
        this.orderPricingList = orderPricingList;
    }

    public List<HourHasPrice> getHourHasPrices() {
        return hourHasPrices;
    }

    public void setHourHasPrices(List<HourHasPrice> hourHasPrices) {
        this.hourHasPrices = hourHasPrices;
    }


}
