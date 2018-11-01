package com.example.demo.config;

public enum PaginationEnum {
    userPageSize(1,5);
    private int id;
    private int numberOfRows;

    PaginationEnum(int id, int numberOfRows) {
       this.id = id;
       this.numberOfRows = numberOfRows;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }
}
