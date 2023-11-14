package com.example.hams;

import java.util.ArrayList;
import java.util.Date;

public class Shift {
    private ArrayList<Appointment> appointments;
    private String startTime;
    private String endTime;
    private String date;

    public Shift(String start, String end, String date){
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
