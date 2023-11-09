package com.example.hams;

import java.util.Date;

public class Shift {

    private Doctor doctor;
    private Appointment appointment;
    private String startTime;
    private String endTime;
    private Date date;

    public Shift(Doctor d, Appointment a, String start, String end, Date date){
        this.doctor = d;
        this.appointment = a;
        this.startTime = start;
        this.endTime = end;
        this.date = date;
    }

    @Override
    public String toString(){
        return appointment.getPatient().toString() +". Start Time: " + startTime + ". End Time: " + endTime+ ". Date: " + date;
    }

}
