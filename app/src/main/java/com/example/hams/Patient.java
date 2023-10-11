package com.example.hams;

public class Patient extends User {

    private String healthCardNumber;

    public Patient(String firstName, String lastName, String username, String password, String phoneNumber, String[] address, String healthCardNumber) {
        super(firstName, lastName, username, password, phoneNumber, address);
        this.healthCardNumber = healthCardNumber;
    }

    // Constructor with no parameters
    public Patient() {
        super();
        this.healthCardNumber = "";
    }

    // Getter and Setters for healthcareNumber

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(String healthcareNumber) {
        this.healthCardNumber = healthcareNumber;
    }
}
