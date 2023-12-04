package com.example.hams;

import java.util.Date;

public class Appointment {

    static final String APPROVED = "approved";
    static final String PENDING = "pending";
    static final String REJECTED = "rejected";
    private String patientUID;
    private String doctorUID;
    private String status;
    private String key;
    private String date;
    private String startTime;
    private String shiftID;

    public Appointment(String patientUID, String doctorUID, String date, String startTime, String shiftID){
        this.patientUID = patientUID;
        this.doctorUID = doctorUID;
        status = PENDING;
        key = "";
        this.date = date;
        this.startTime = startTime;
        this.shiftID = shiftID;
    }

    public Appointment(){
        patientUID = "";
        doctorUID = "";
        status = PENDING;
        key = "";
        date = "";
        startTime = "";
    }

    public String getPatientUID(){
        return patientUID;
    }

    public String getDoctorUID(){return doctorUID;}

    public String getStatus(){return status;}

    public String getKey() {
        return key;
    }

    public String getDate(){
        return date;
    }

    public String getStartTime(){return startTime;}

    public String getShiftID(){return shiftID;}

    public void setShiftID(String shiftID){this.shiftID = shiftID;}

    public void setPatientUID(String patientUID){this.patientUID = patientUID;}

    public void setDoctorUID(String doctorUID){this.doctorUID = doctorUID;}

    public void setStatus(String status){this.status = status;}

    public void setKey(String key){this.key = key;}

    public void setDate(String date){this.date = date;}

    public void setStartTime(String startTime){this.startTime = startTime;}

    @Override
    public String toString(){
        return "Start Time: " + startTime + ". Date: " + date + ". Status: " + status;
    }

}
