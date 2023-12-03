package com.example.hams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        //Setting top bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Patient Screen: Appointments");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //calling objects needed for authentication and reading from the DB
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayList<Appointment> upcomingAppointmentList = new ArrayList<>();
        ArrayList<Appointment> previousAppointmentList = new ArrayList<>();
        ListView upcomingListView = (ListView) findViewById(R.id.appointments2);
        ListView previousListView = (ListView) findViewById(R.id.pastAppointments2);
        RatingBar rateDoctor = (RatingBar) findViewById(R.id.ratingBar);

        //Getting user ID of the currently logged in user
        String userId = mAuth.getUid();

        //getting the type of user
        mDatabase.child("users").child(userId).child("type").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                //Once type is found update the welcome text field to indicate type
                String type = task.getResult().getValue(String.class);
            }
        });


        //Get all appointments from database.
        //If the patient UID attribute of the appointment matches the one of the patient logged in, consider it.
        //Check if the appointment has passed or not.
        //If it has passed, add it to the passed list, otherwise, put it in the upcoming list.

        //In the upcoming list, add code to select the list and cancel it if the cancel button is pressed.
        //If the cancel button is pressed, remove it from the list and set the patient UID to empty.









        buttonLogin.setOnClickListener(new View.OnClickListener(){
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view){
                mAuth.signOut();
                openLoginScreen();
            }
        });

        addA.setOnClickListener(new View.OnClickListener(){
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view){
                openAddScreen();
            }
        });

        rating.setOnClickListener(new View.OnClickListener(){
            //Upon clicking, submit a rating to the doctor that hosted the select appointment.
            public void onClick(View view){
                int numberOfStars = rateDoctor.getNumStars();
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