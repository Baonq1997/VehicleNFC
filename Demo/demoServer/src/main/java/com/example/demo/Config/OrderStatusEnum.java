package com.example.demo.config;

public enum OrderStatusEnum {
    Open(1,"Open"), Close(2,"close"), Refund(3,"refund");

    private int id;
    private String name;

    OrderStatusEnum(int id, String name) {
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
}
