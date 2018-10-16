package Sqlite;

import java.io.Serializable;

public class History implements Serializable {

    private int id;
    private double total;
    private int check_in_date;
    private int check_out_date;
    private int duration;
    private int allowed_parking_from;
    private int allowed_parking_to;
    private String tbl_order_status_id;
    private String username;
    private String vehical_name;
    private String vehical_id;
    private String tbl_location_id;

    public History() {
    }

    public History(int id, double total, int check_in_date, int check_out_date, int duration, int allowed_parking_from, int allowed_parking_to, String tbl_order_status_id, String username, String vehical_name, String vehical_id, String tbl_location_id) {
        this.id = id;
        this.total = total;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.duration = duration;
        this.allowed_parking_from = allowed_parking_from;
        this.allowed_parking_to = allowed_parking_to;
        this.tbl_order_status_id = tbl_order_status_id;
        this.username = username;
        this.vehical_name = vehical_name;
        this.vehical_id = vehical_id;
        this.tbl_location_id = tbl_location_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getCheck_in_date() {
        return check_in_date;
    }

    public void setCheck_in_date(int check_in_date) {
        this.check_in_date = check_in_date;
    }

    public int getCheck_out_date() {
        return check_out_date;
    }

    public void setCheck_out_date(int check_out_date) {
        this.check_out_date = check_out_date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getAllowed_parking_from() {
        return allowed_parking_from;
    }

    public void setAllowed_parking_from(int allowed_parking_from) {
        this.allowed_parking_from = allowed_parking_from;
    }

    public int getAllowed_parking_to() {
        return allowed_parking_to;
    }

    public void setAllowed_parking_to(int allowed_parking_to) {
        this.allowed_parking_to = allowed_parking_to;
    }

    public String getTbl_order_status_id() {
        return tbl_order_status_id;
    }

    public void setTbl_order_status_id(String tbl_order_status_id) {
        this.tbl_order_status_id = tbl_order_status_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVehical_name() {
        return vehical_name;
    }

    public void setVehical_name(String vehical_name) {
        this.vehical_name = vehical_name;
    }

    public String getVehical_id() {
        return vehical_id;
    }

    public void setVehical_id(String vehical_id) {
        this.vehical_id = vehical_id;
    }

    public String getTbl_location_id() {
        return tbl_location_id;
    }

    public void setTbl_location_id(String tbl_location_id) {
        this.tbl_location_id = tbl_location_id;
    }
}
