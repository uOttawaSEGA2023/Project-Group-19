package com.example.hams;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class User {

    static final String APPROVED = "approved";
    static final String PENDING = "pending";
    static final String REJECTED = "rejected";

    // Attributes
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String status;

    @Exclude
    private String password;


    //Address is a class storing address, postal code, country, province and city:
    private Address address;

    public User(String firstName, String lastName, String username, String password,
                String phoneNumber, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        status = PENDING;
    }

    // Constructor with no parameters
    public User() {
        this.firstName = "";
        this.lastName = "";
        this.username = "";
        this.password = "";
        this.phoneNumber = "";
    }

    // Getters and Setters for each attribute

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "User: " +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'';
    }

}
