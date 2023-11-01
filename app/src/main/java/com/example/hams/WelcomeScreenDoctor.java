package com.example.hams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class WelcomeScreenDoctor extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_doctor);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Doctor Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}