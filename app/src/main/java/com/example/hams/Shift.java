package com.example.hams;

import java.util.ArrayList;
import java.util.Date;

public class Shift {

    private Doctor doctor;
    private ArrayList<Appointment> appointments;
    private String startTime;
    private String endTime;
    private Date date;

    public Shift(Doctor d, String start, String end, Date date){
        this.doctor = d;
        this.appointments = new ArrayList<Appointment>();
        this.startTime = start;
        this.endTime = end;
        this.date = date;
    }

    @Override
    public String toString(){
        return "Start Time: " + startTime + ". End Time: " + endTime+ ". Date: " + date;
    }

}
