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

    TextView welcome;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        Button buttonLogin = findViewById(R.id.logout);
        welcome = findViewById(R.id.welcome);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Home Page");
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String userId = mAuth.getUid();

        mDatabase.child("users").child(userId).child("type").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String type = task.getResult().getValue(String.class);
                String displayText = "Welcome! You are logged in as a " + type;
                welcome.setText(displayText);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                mAuth.signOut();
                openLoginScreen();
            }
        });


    }

    public void openLoginScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}