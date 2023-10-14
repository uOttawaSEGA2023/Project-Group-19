package com.example.hams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        //finding buttons and text field
        Button buttonLogin = findViewById(R.id.logout);
        TextView welcome = findViewById(R.id.welcome);

        //Setting top bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Home Page");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //calling objects needed for authentication and reading from the DB
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        //Getting user ID of the currently logged in user
        String userId = mAuth.getUid();

        //getting the type of user
        mDatabase.child("users").child(userId).child("type").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Once type is found update the welcome text field to indicate type
                String type = task.getResult().getValue(String.class);
                String displayText = "Welcome! You are logged in as a " + type;
                welcome.setText(displayText);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view){
                mAuth.signOut();
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