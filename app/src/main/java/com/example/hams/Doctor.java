package com.example.hams;

import java.util.ArrayList;

public class Doctor extends User {

    // Attributes
    private String employeeNumber;
    private ArrayList<String> specialties;
    private ArrayList<Integer> ratings;

    private boolean autoApproveSetting;

    public Doctor(String firstName, String lastName, String username, String password,
                  String phoneNumber, Address address, String employeeNumber,
                  ArrayList<String> specialties, String userID) {

        super(firstName, lastName, username, password, phoneNumber, address, userID);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
        this.autoApproveSetting = false;
        this.ratings = new ArrayList<Integer>();
    }

    // Constructor with no parameters
    public Doctor() {
        super();
        this.employeeNumber = "";
        this.specialties = null;
        this.autoApproveSetting = false;
        this.ratings = new ArrayList<Integer>();
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

    public boolean getAutoApproveSetting() {
        return autoApproveSetting;
    }

    public void setAutoApproveSetting(boolean approvePaitents) {
        this.autoApproveSetting = approvePaitents;
    }

    public void setSpecialties(ArrayList<String> specialties) {
        this.specialties = specialties;
    }

    public double getAvgRating(){
        double sum = 0;
        for(int i = 0; i < ratings.size(); i++){
            sum = sum + ratings.get(i);
        }
        return sum;
    }

    public void addRating(int num){
        ratings.add(num);
    }

    @Override
    public String toString(){
        return "Doctor: " + getFirstName();
    }

}

