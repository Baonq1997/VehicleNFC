package com.example.demo.model;

import java.util.List;

public class HourHasPrice {
    private int hour;
    private Double price;
    private Double fine;
    private Double total;
    private Integer minutes;
    private boolean isLate = false;
    private boolean fullHour = true;

    public HourHasPrice(int hour, Double price) {
        this.hour = hour;
        this.price = price;
    }

    public HourHasPrice(int hour, Double price, Double fine, Double total, Integer minutes, boolean fullHour) {
        this.hour = hour;
        this.price = price;
        this.fine = fine;
        this.total = total;
        this.minutes = minutes;
        this.fullHour = fullHour;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int compare(HourHasPrice hourHasPrice) {
        return (this.getHour() < hourHasPrice.getHour()) ? -1 : 1;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public static List<HourHasPrice> sort(List<HourHasPrice> hourHasPrices) {
        hourHasPrices.sort((o1, o2) -> o1.compare(o2));
        return hourHasPrices;
    }

    public Double getFine() {
        return fine;
    }

    public void setFine(Double fine) {
        this.fine = fine;
    }

    public boolean isFullHour() {
        return fullHour;
    }

    public void setFullHour(boolean fullHour) {
        this.fullHour = fullHour;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }
}
