package com.example.hams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class WelcomeScreen extends AppCompatActivity {

    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Home Page");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button buttonLogin = findViewById(R.id.logout);

        welcome = findViewById(R.id.welcome);

        welcome.setText("Welcome! You are logged in as ");

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openLoginScreen();
            }
        });


    }

    public void openLoginScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}