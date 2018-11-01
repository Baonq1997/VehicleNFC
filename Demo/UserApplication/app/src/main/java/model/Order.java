package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Swomfire on 24-Sep-18.
 */

public class Order {
    @SerializedName("id")
    @Expose
    private int id;


    @SerializedName("checkInDate")
    @Expose
    private long checkInDate;

    @SerializedName("checkOutDate")
    @Expose
    private long checkOutDate;

    @SerializedName("dateEnded")
    @Expose
    private long dateEnded;

    @SerializedName("duration")
    @Expose
    private long duration;

    @SerializedName("allowedParkingFrom")
    @Expose
    private long allowedParkingFrom;

    @SerializedName("allowedParkingTo")
    @Expose
    private long allowedParkingTo;

    @SerializedName("total")
    @Expose
    private double total;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("minHour")
    @Expose
    private int minHour;

    @SerializedName("locationId")
    @Expose
    private Location location;

    @SerializedName("userId")
    @Expose
    private User user;

    @SerializedName("orderStatusId")
    @Expose
    private OrderStatus orderStatus;

    @SerializedName("orderPricingList")
    @Expose
    private List<OrderPricing> orderPricings;

    public long getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(long checkInDate) {
        this.checkInDate = checkInDate;
    }

    public long getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(long checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public long getDateEnded() {
        return dateEnded;
    }

    public void setDateEnded(long dateEnded) {
        this.dateEnded = dateEnded;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<OrderPricing> getOrderPricings() {
        return orderPricings;
    }

    public void setOrderPricings(List<OrderPricing> orderPricings) {
        this.orderPricings = orderPricings;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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


    public int getMinHour() {
        return minHour;
    }

    public void setMinHour(int minHour) {
        this.minHour = minHour;
    }
}
