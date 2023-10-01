package com.example.hams;

public class Patient extends User {

    private String healthCardNumber;

    public Patient(String healthCardNumber) {
        super();
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
