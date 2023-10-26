package com.example.hams;

import java.util.ArrayList;

public class Doctor extends User {

    // Attributes
    private String employeeNumber;
    private ArrayList<String> specialties;

    public Doctor(String firstName, String lastName, String username, String password,
                  String phoneNumber, Address address, String employeeNumber,
                  ArrayList<String> specialties, String userID) {

        super(firstName, lastName, username, password, phoneNumber, address, userID);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
    }

    // Constructor with no parameters
    public Doctor() {
        super();
        this.employeeNumber = "";
        this.specialties = null;
    }

    // Getters and Setters for each attribute

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public ArrayList<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(ArrayList<String> specialties) {
        this.specialties = specialties;
    }

}

