package com.example.hams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


        logout.setOnClickListener(new View.OnClickListener() {
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view) {
                openLoginScreen();
            }
        });

        ArrayList<Appointment> appointmentList = new ArrayList<>();
        AppointmentAdapter adapter = new AppointmentAdapter(WelcomeScreenDoctor.this, appointmentList);
        ListView listView = (ListView) findViewById(R.id.appointments);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Code to run with a selected item from the shifts list.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Appointment appointment = (Appointment) parent.getItemAtPosition(position);

                Toast.makeText(WelcomeScreenDoctor.this, "Selected Appointment: " + appointment.toString(), Toast.LENGTH_SHORT).show();

                approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updateAppointmentStatus(appointment, ref, Appointment.APPROVED);
                    }
                });

                //reject button functionality
                reject.setOnClickListener(new View.OnClickListener() {
                    //Upon clicking the reject button, appointment will dissapear from list
                    public void onClick(View view) {
                        adapter.remove(appointment);
                        updateAppointmentStatus(appointment, ref, Appointment.REJECTED);

                    }
                });

                //patient information functionality
                patientInformation.setOnClickListener(new View.OnClickListener() {
                    //Upon clicking the patientInformation button, display patient info on screen
                    public void onClick(View view) {
                        setContentView(R.layout.patient_info);
                        Patient p = appointment.getPatient();
                        TextView title = findViewById(R.id.userRequest2);
                        title.setText(p.toString() + "'s Information");
                        TextView firstName = findViewById(R.id.firstNameList2);
                        firstName.setText("First Name: " + p.getFirstName());

                        TextView lastName = findViewById(R.id.lastNameList2);
                        lastName.setText("Last Name: " + p.getLastName());

                        TextView email = findViewById(R.id.emailList2);
                        email.setText("Email Address: " + p.getUsername());

                        TextView phone = findViewById(R.id.phoneList2);
                        phone.setText("Phone Number: " + p.getPhoneNumber());

                        TextView address = findViewById(R.id.addressList4);
                        address.setText(p.getAddress().toString());
                        TextView health = findViewById(R.id.numberList2);
                        health.setText("Health Card Number: " + p.getHealthCardNumber());
                    }
                });

            }
        });
        ArrayList<Appointment> appointmentListPast = new ArrayList<>();
        AppointmentAdapter adapterPast = new AppointmentAdapter(WelcomeScreenDoctor.this, appointmentListPast);
        ListView listViewPast = (ListView) findViewById(R.id.appointments);
        listViewPast.setAdapter(adapter);



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

    public void updateAppointmentStatus(Appointment appointment, DatabaseReference ref, String status) {
        Patient patient = appointment.getPatient();
        //Update status variable
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        ref.child("appointments").child(patient.getUserID()).child(appointment.getKey()).updateChildren(map);
        Toast.makeText(WelcomeScreenDoctor.this, status + " Shift: " + appointment.toString(), Toast.LENGTH_SHORT).show();
    }

    public boolean compareDate(Appointment appointment){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

        }
    }

    /**
     * //Method to enable the back button on the patient information page to return to the doctor welcome screen.
     *
     * @param item The item
     * @return If the return was successful.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), WelcomeScreenDoctor.class);
        startActivity(intent);
        return true;
    }


}