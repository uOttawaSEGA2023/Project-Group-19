package com.example.hams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WelcomeScreenDoctor extends AppCompatActivity {
    Boolean auto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String doctorUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_doctor);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Doctor Account: Appointments");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button logout = findViewById(R.id.logoutDoctor); //Button to logout the doctor.
        Button shifts = findViewById(R.id.toShifts); //Button to view shifts.
        Button autoApprove = findViewById(R.id.approveAll); //Button to enable auto approve.
        Button patientInformation = findViewById(R.id.getInfoPatient); //Button to see patient information.
        Button approve = findViewById(R.id.approveAppointment); //Button to approve an appointment.
        Button reject = findViewById(R.id.rejectAppointment); //Button to reject an appointment.

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        Query appointmentQuery = ref.child("appointments").orderByChild("doctorUID").equalTo(doctorUID);

        ArrayList<Appointment> upcomingAppointmentList = new ArrayList<>();
        ArrayList<Appointment> previousAppointmentList = new ArrayList<>();
        ListView upcomingListView = (ListView) findViewById(R.id.appointments);
        ListView previousListView = (ListView) findViewById(R.id.pastAppointments);

        // Initializes the two lists with appointments
        appointmentQuery.addChildEventListener(new ChildEventListener() {
            AppointmentAdapter adapter;

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Appointment appointment = snapshot.getValue(Appointment.class);
                try{
                    // check if the appointment is an upcoming one and add it to the list based on that
                    // the listView must be updated each time or else the changes will not be displayed on the app
                    if(isUpcomingAppointment(appointment)){
                        upcomingAppointmentList.add(appointment);
                        adapter = new AppointmentAdapter(WelcomeScreenDoctor.this, upcomingAppointmentList);
                        upcomingListView.setAdapter(adapter);
                    }
                    else if(appointment.getStatus().equals(Appointment.APPROVED)){
                        previousAppointmentList.add(appointment);
                        adapter = new AppointmentAdapter(WelcomeScreenDoctor.this, previousAppointmentList);
                        previousListView.setAdapter(adapter);
                    }

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Appointment appointment = snapshot.getValue(Appointment.class);
                // when a child is changed we find the changed child in the listView and update it
                // no need to check in the previous appointment list because that list cannot be updated
                for(int i = 0; i < upcomingAppointmentList.size(); i++){
                    if (upcomingAppointmentList.get(i).getKey().equals(appointment.getKey())){
                        upcomingAppointmentList.set(i, appointment);

                        adapter = new AppointmentAdapter(WelcomeScreenDoctor.this, upcomingAppointmentList);
                        upcomingListView.setAdapter(adapter);

                        return;
                    }
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        logout.setOnClickListener(new View.OnClickListener() {
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view) {
                openLoginScreen();
            }
        });

        upcomingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Code to run with a selected item from the upcoming appointments list.
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
                        // no adapter object oustide of the query so we must change the list and initialize a new adapter when something
                        // is removed
                        upcomingAppointmentList.remove(appointment);
                        upcomingListView.setAdapter(new AppointmentAdapter(WelcomeScreenDoctor.this, upcomingAppointmentList));
                        // rejected appointments are simply removed from the database
                        ref.child("appointments").child(appointment.getKey()).removeValue();
                    }
                });

                //patient information functionality
                patientInformation.setOnClickListener(new View.OnClickListener() {
                    //Upon clicking the patientInformation button, display patient info on screen
                    public void onClick(View view) {
                        setContentView(R.layout.patient_info);
                        String patientUID = appointment.getPatientUID();

                        ref.child("users").child(patientUID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(Task<DataSnapshot> task) {

                                Patient p = task.getResult().getValue(Patient.class);
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
                ref.child("users").child(doctorUID).child("autoApproveSetting").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(Task<DataSnapshot> task) {
                        auto = task.getResult().getValue(Boolean.class);
                        if(!auto){Map<String, Object> map = new HashMap<>(); //If the doctor does not have auto approve on, turn it on.
                            map.put("autoApproveSetting", true);
                            ref.child("users").child(doctorUID).updateChildren(map);
                            Toast.makeText(WelcomeScreenDoctor.this, "Auto Approve Turned On", Toast.LENGTH_SHORT).show();
                        }else{ //Otherwise, turn auto approve off.
                            Map<String, Object> map = new HashMap<>();
                            map.put("autoApproveSetting", false);
                            ref.child("users").child(doctorUID).updateChildren(map);
                            Toast.makeText(WelcomeScreenDoctor.this, "Auto Approve Turned Off", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    /**
     * Method to show the doctor shift related information.
     */
    public void openShiftsScreen() {
        Intent intent = new Intent(this, DoctorShifts.class);
        startActivity(intent);
    }

    public void updateAppointmentStatus(Appointment appointment, DatabaseReference ref, String status) {
        String key = appointment.getKey();
        //Update status variable
        Map<String, Object> map = new HashMap<>();
        map.put("status", status);
        ref.child("appointments").child(key).updateChildren(map);
        Toast.makeText(WelcomeScreenDoctor.this, status + " Shift: " + appointment.toString(), Toast.LENGTH_SHORT).show();
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

    // returns true if the current date is less than the appointment's date
    private boolean isUpcomingAppointment(Appointment appointment) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();

        Date appointmentDate = dateFormat.parse(appointment.getDate());
        Date appointmentTime = timeFormat.parse(appointment.getStartTime());
        Date currentDate = dateFormat.parse(dateFormat.format(calendar.getTime()));
        Date currentTime = timeFormat.parse(timeFormat.format(calendar.getTime()));
        // Will return true for appointments on the same day that are at a later time as well
        return currentDate.compareTo(appointmentDate) <= 0 && currentTime.compareTo(appointmentTime) <= 0;
    }

}