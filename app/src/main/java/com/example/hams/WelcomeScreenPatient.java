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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class WelcomeScreenPatient extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //calling objects needed for authentication and reading from the DB
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_patient);

        //Setting top bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Patient Screen: Appointments");
        actionBar.setDisplayHomeAsUpEnabled(true);


        //setting up the dropdown of doctor specialities

        //reference to the dropdown
        Spinner spinner = findViewById(R.id.dropdown);

        //fills our dropdown with text
        ArrayAdapter<CharSequence> dropdownAdapter = ArrayAdapter.createFromResource(this, R.array.specializations, android.R.layout.simple_spinner_item);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dropdownAdapter);

        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), text, Toast.LENGTH_SHORT).show();

        ArrayList<Appointment> AppointmentSlots = new ArrayList<>();
        ListView upcomingListView = (ListView) findViewById(R.id.appointmentSlots);

        Query shiftQuery = mDatabase.child("shifts").orderByChild("doctorUID");

        switch (text) {
            case "Internal Medicine":
                //display available slots for this specialization
                break;
            case "Obstetrics":

                break;
            case "Family Medicine":

                break;
            case "Gynecology":

                break;
            case "Pediatrics":

                break;
            case "Neurology":
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}