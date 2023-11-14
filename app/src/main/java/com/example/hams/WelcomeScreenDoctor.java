package com.example.hams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class WelcomeScreenDoctor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_doctor);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Doctor Account");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button logout = findViewById(R.id.logoutDoctor);
        Button shifts = findViewById(R.id.toShifts);
        Button autoApprove = findViewById(R.id.approveAll);
        Button patientInformation = findViewById(R.id.getInfoPatient);
        Button approve = findViewById(R.id.approveAppointment);
        Button reject = findViewById(R.id.rejectAppointment);




        logout.setOnClickListener(new View.OnClickListener() {
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view) {
                openLoginScreen();
            }
        });

        //patient information functionality
        patientInformation.setOnClickListener(new View.OnClickListener() {
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view) {

            }
        });

        //reject button functionality
        reject.setOnClickListener(new View.OnClickListener() {
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view) {

            }
        });
        //approve button functionality
        approve.setOnClickListener(new View.OnClickListener() {
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view) {

            }
        });


        shifts.setOnClickListener(new View.OnClickListener() {
            //Upon clicking the view shifts button, bring the doctor to view shift related information.
            public void onClick(View view) {
                openShiftsScreen();
            }
        });

        autoApprove.setOnClickListener(new View.OnClickListener() {
            //Upon clicking the auto approve requests button, make it do this doctor has all appointment requests approved automatically.
            public void onClick(View view) {

            }
        });


    }

    /**
     * Opens the login screen.
     */
    public void openLoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openShiftsScreen() {
        Intent intent = new Intent(this, DoctorShifts.class);
        startActivity(intent);
    }


}