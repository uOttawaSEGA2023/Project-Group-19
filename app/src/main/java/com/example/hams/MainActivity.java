package com.example.hams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText emailLogin;
    EditText passwordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Login Page");
        Button welcomeLogin = findViewById(R.id.login);
        Button buttonDoctorLogin = findViewById(R.id.registerDoctorLogin);
        Button buttonPatientLogin = findViewById(R.id.registerPatientLogin);

        Admin firstAdmin = new Admin("Adam", "123456");

        buttonDoctorLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openDoctorForm();
            } //Opens the doctor registration form.
        });

        buttonPatientLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openPatientForm();
            } //Opens the patient registration form.
        });

        welcomeLogin.setOnClickListener(new View.OnClickListener(){ //Arn use this! It's the login button functionality.
            public void onClick(View view){
                openWelcomeScreen();
            }
        });

    }
    public void openDoctorForm(){
        Intent intent = new Intent(this, DoctorForm.class);
        startActivity(intent);
    }

    public void openPatientForm(){
        Intent intent = new Intent(this, PatientForm.class);
        startActivity(intent);
    }

    public void openWelcomeScreen(){
        Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
    }
}