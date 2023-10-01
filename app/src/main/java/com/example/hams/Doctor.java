package com.example.hams;

import java.util.List;

public class Doctor extends User {

    // Attributes
    private String employeeNumber;
    private List<String> specialties;

    public Doctor(String employeeNumber, List<String> specialties) {
        super();
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

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }

}

