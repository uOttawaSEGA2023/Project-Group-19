package com.example.hams;

import java.util.ArrayList;
import java.util.Date;

public class Shift {

    private ArrayList<Appointment> appointments;
    private String startTime;
    private String endTime;
    private String date;
    private String key;

    public Shift(String start, String end, String date){
        this.appointments = new ArrayList<Appointment>();
        this.startTime = start;
        this.endTime = end;
        this.date = date;
        this.key = "";
    }

    public Shift(){
        appointments = null;
        startTime = "";
        endTime = "";
        date = "";
        key = "";
    }

    public String getStartTime(){
        return startTime;
    }

    public String getEndTime(){
        return endTime;
    }

    public String getDate(){
        return date;
    }

    public String getKey(){
        return key;
    }

    public ArrayList<Appointment> getAppointments(){
        return appointments;
    }

    public void setStartTime (String startTime){
        this.startTime = startTime;
    }

    public void setEndTime (String endTime){
        this.endTime = endTime;
    }

    public void setKey(String key){
        this.key = key;
    }


    @Override
    public String toString(){
        return "Start Time: " + startTime + ". End Time: " + endTime+ ". Date: " + date;
    }

}