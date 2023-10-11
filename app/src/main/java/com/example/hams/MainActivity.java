package com.example.hams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS");

        Button button = findViewById(R.id.button);

        Admin firstAdmin = new Admin("Adam", "123456");

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openDoctorForm();
            }
        });

    }
    public void openDoctorForm(){
        Intent intent = new Intent(this, DoctorForm.class);
        startActivity(intent);
    }
}