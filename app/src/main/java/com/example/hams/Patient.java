package com.example.hams;

import java.util.ArrayList;

public class Patient extends User {

    private String healthCardNumber;
    private ArrayList<Appointment> appointments;

    public Patient(String firstName, String lastName, String username, String password,
                   String phoneNumber, Address address, String healthCardNumber, String userID) {
        super(firstName, lastName, username, password, phoneNumber, address, userID);
        this.healthCardNumber = healthCardNumber;
        this.appointments = new ArrayList<Appointment>();
    }

    // Constructor with no parameters
    public Patient() {
        super();
        this.healthCardNumber = "";
        this.appointments = null;
    }

    // Getter and Setters for healthcareNumber

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(String healthcareNumber) {
        this.healthCardNumber = healthcareNumber;
    }

    @Override
    public String toString(){
        return "Patient: " + getFirstName();
    }
}
