package com.example.hams;

public class Appointment {

    static final String APPROVED = "approved";
    static final String PENDING = "pending";
    static final String REJECTED = "rejected";
    private Patient patient;
    private String status;
    private String key;

    public Appointment(Patient p){
        this.patient = p;
        status = PENDING;
        key = "";
    }

    public Patient getPatient(){
        return patient;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString(){
        return getPatient().toString() + ", "+ status;
    }

}
