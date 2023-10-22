package com.example.hams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreenAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_admin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Admin Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button buttonLogin = findViewById(R.id.logoutAdmin);

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view){
                openLoginScreen();
            }
        });
    }

    /**
     * Opens the login screen.
     */
    public void openLoginScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}