package com.example.hams;

public class Appointment {

    static final String APPROVED = "approved";
    static final String PENDING = "pending";
    static final String REJECTED = "rejected";
    private Patient patient;
    private String status;

    public Appointment(Patient p){
        this.patient = p;
        status = PENDING;
    }

    public Patient getPatient(){
        return patient;
    }

    @Override
    public String toString(){
        return getPatient().toString() + ", "+ status;
    }
}
