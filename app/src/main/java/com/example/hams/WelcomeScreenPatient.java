package com.example.hams;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Type;
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
        String specialty = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), specialty, Toast.LENGTH_SHORT).show();

        ArrayList<Appointment> AppointmentSlots = new ArrayList<>();
        ListView slotsListView = (ListView) findViewById(R.id.appointmentSlots);

        Query doctorQuery = mDatabase.child("users").orderByChild("type").equalTo("doctor");
        Query appointmentQuery = mDatabase.child("appointments").orderByChild("patientUID").equalTo("");
        ArrayList<Appointment> appointmentList = new ArrayList<>();

        doctorQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> doctorList = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Doctor doctor = userSnapshot.getValue(Doctor.class);

                    if (doctor.getSpecialties().contains(specialty)) {
                        doctorList.add(doctor.getUserID());
                    }
                }
                appointmentQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            Appointment appointment = userSnapshot.getValue(Appointment.class);

                            if (doctorList.contains(appointment.getDoctorUID())) {
                                appointmentList.add(appointment);
                            }
                        }
                        AppointmentAdapter adapter = new AppointmentAdapter(WelcomeScreenPatient.this, appointmentList);

                        slotsListView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * //Method to enable the back button on the patient information page to return to the doctor welcome screen.
     *
     * @param item The item
     * @return If the return was successful.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
        startActivity(intent);
        return true;
    }
}