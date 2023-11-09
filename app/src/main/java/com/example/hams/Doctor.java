package com.example.hams;

import java.util.ArrayList;

public class Doctor extends User {

    // Attributes
    private String employeeNumber;
    private ArrayList<String> specialties;

    private ArrayList<Shift> shifts;
    private boolean approve;

    public Doctor(String firstName, String lastName, String username, String password,
                  String phoneNumber, Address address, String employeeNumber,
                  ArrayList<String> specialties, String userID) {

        super(firstName, lastName, username, password, phoneNumber, address, userID);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
        this.shifts = new ArrayList<Shift>();
        this.approve = false;
    }

    // Constructor with no parameters
    public Doctor() {
        super();
        this.employeeNumber = "";
        this.specialties = null;
        this.shifts = null;
        this.approve = false;
    }

    // Getters and Setters for each attribute

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }
    public void setAutoApprove(Boolean b){
        this.approve = b;
    }
    public void addShift(Shift shift){
        this.shifts.add(shift);
    }

    public ArrayList<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(ArrayList<String> specialties) {
        this.specialties = specialties;
    }

    @Override
    public String toString(){
        return "Doctor: " + getFirstName();
    }

}

