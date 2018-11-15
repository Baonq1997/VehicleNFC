package com.example.demo.config;

public enum RefundStatusEnum {
    Open(1, "Open"), Approved(2, "Approved"), Rejected(3, "Rejected");

    private int id;
    private String name;

    RefundStatusEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static RefundStatusEnum approveEnum(boolean isApprve) {
        return (isApprve) ? Approved : Rejected;
    }
}
