package com.example.hams;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        //finding buttons and text field
        Button buttonLogin = findViewById(R.id.logout);
        Button addA = findViewById(R.id.toAdd);
        Button rating = findViewById(R.id.submitRating);
        Button cancel = findViewById(R.id.cancelAppointment);

        //Setting top bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Patient Screen: Appointments");
        actionBar.setDisplayHomeAsUpEnabled(true);

        ArrayList<Appointment> upcomingAppointmentList = new ArrayList<>();
        ArrayList<Appointment> previousAppointmentList = new ArrayList<>();

        ListView upcomingListView = (ListView) findViewById(R.id.appointments2);
        ListView previousListView = (ListView) findViewById(R.id.pastAppointments2);

        RatingBar rateDoctor = (RatingBar) findViewById(R.id.ratingBar);

        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        String patientUID = userAuth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        Query appointmentQuery = ref.child("appointments").orderByChild("patientUID").equalTo(patientUID);

        //In the upcoming list, add code to select the list and cancel it if the cancel button is pressed.
        //If the cancel button is pressed, remove it from the list and set the patient UID to empty.

        appointmentQuery.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Appointment appointment = snapshot.getValue(Appointment.class);
                try{
                    AppointmentAdapter adapter;
                    // check if the appointment is an upcoming one and add it to the list based on that
                    // the listView must be updated each time or else the changes will not be displayed on the app
                    if(isUpcomingAppointment(appointment)){
                        upcomingAppointmentList.add(appointment);
                        adapter = new AppointmentAdapter(WelcomeScreen.this, upcomingAppointmentList);
                        upcomingListView.setAdapter(adapter);
                    }
                    else if(appointment.getStatus().equals(Appointment.APPROVED)){
                        previousAppointmentList.add(appointment);
                        adapter = new AppointmentAdapter(WelcomeScreen.this, previousAppointmentList);
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

                        AppointmentAdapter adapter = new AppointmentAdapter(WelcomeScreen.this, upcomingAppointmentList);
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

        upcomingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Code to run with a selected item from the upcoming appointments list.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Appointment appointment = (Appointment) parent.getItemAtPosition(position);

                Toast.makeText(WelcomeScreen.this, "Selected Appointment: " + appointment.toString(), Toast.LENGTH_SHORT).show();

                cancel.setOnClickListener(new View.OnClickListener(){
                    //Upon clicking, cancel this appointment.
                    public void onClick(View view){
                        appointment.setPatientUID("");
                        //appointment.removeClaim();
                        //remove from list view.
                    }
                });

            }
        });

        previousListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Code to run with a selected item from the upcoming appointments list.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Appointment appointment = (Appointment) parent.getItemAtPosition(position);

                Toast.makeText(WelcomeScreen.this, "Selected Appointment: " + appointment.toString(), Toast.LENGTH_SHORT).show();

                rateDoctor.setOnClickListener(new View.OnClickListener(){
                    //Upon clicking, submit a rating to the doctor that hosted the select appointment.
                    public void onClick(View view){
                        int numberOfStars = rateDoctor.getNumStars();
                        //Get doctor.
                        //Run add rating method to the doctor object.
                        //Update in database.
                    }
                });

            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view){
                userAuth.signOut();
                openLoginScreen();
            }
        });

        addA.setOnClickListener(new View.OnClickListener(){
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view){
                openAddScreen();
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

    public void openAddScreen(){
        Intent intent = new Intent(this, WelcomeScreenPatient.class);
        startActivity(intent);
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

        // if the current date comes before the appointment date return true
        if (currentDate.compareTo(appointmentDate) < 0){
            return true;
        }
        // otherwise return true if the current date is the same as the appointment date and the current time is before
        else return currentDate.compareTo(appointmentDate) == 0 && currentTime.compareTo(appointmentTime) < 0;
    }

}