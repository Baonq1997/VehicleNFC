package model;

import java.util.List;

public class HourHasPrice {
    int hour;
    Double price;
    Double total;

    public HourHasPrice(int hour, Double price) {
        this.hour = hour;
        this.price = price;
    }

    public HourHasPrice(int hour, Double price, Double total, int flag) {
        this.hour = hour;
        this.price = price;
        this.total = total;
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

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public static List<HourHasPrice> sortList(List<HourHasPrice> hourHasPrices) {
        for (int i = 0; i < hourHasPrices.size(); i++) {
            for (int j = hourHasPrices.size() - 1; j > i; j--) {
                if (hourHasPrices.get(i).getHour() > hourHasPrices.get(j).getHour()) {
                    HourHasPrice tmp = hourHasPrices.get(i);
                    hourHasPrices.set(i,hourHasPrices.get(j));
                    hourHasPrices.set(j,tmp);
                }
            }
        }
        return hourHasPrices;
    }
}
