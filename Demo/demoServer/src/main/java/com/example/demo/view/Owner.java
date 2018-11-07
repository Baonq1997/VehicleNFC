package com.example.demo.view;

import com.example.demo.component.user.User;

import java.io.Serializable;


public class Owner implements Serializable {

    private String phoneNumber;

    private double money;

    private String firstName;

    private String lastName;

    private Boolean isActivated;

    public Owner() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }

    public static Owner covertUserToOwner(User user) {
        Owner owner = new Owner();
        owner.setPhoneNumber(user.getPhoneNumber());
        owner.setFirstName(user.getFirstName());
        owner.setLastName(user.getLastName());
        owner.setMoney(user.getMoney());
        owner.setActivated(user.getActivated());
        return owner;
    }
}
