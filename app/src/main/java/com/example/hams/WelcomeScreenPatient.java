package com.example.hams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WelcomeScreenPatient extends AppCompatActivity {

    String[] doctorSpecialties = {"Internal Medicine", "Obstetrics", "Family Medicine", "Gynecology", "Pediatrics", "Neurology"};

    AutoCompleteTextView autoCompleteTextView;

    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_patient);

        //Setting top bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Patient Screen: Appointments");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //calling objects needed for authentication and reading from the DB
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        //setting up the dropdown of doctor specialities

        autoCompleteTextView = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>( this, R.layout.list_item, doctorSpecialties);

        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String speciality = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(WelcomeScreenPatient.this, "Selected: " + speciality, Toast.LENGTH_SHORT).show();
            }
        });



    }

}