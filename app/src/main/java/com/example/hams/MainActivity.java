package com.example.hams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    EditText emailLogin;
    EditText passwordLogin;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Login Page");
        Button welcomeLogin = findViewById(R.id.login);
        Button buttonDoctorLogin = findViewById(R.id.registerDoctorLogin);
        Button buttonPatientLogin = findViewById(R.id.registerPatientLogin);

        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);

        mAuth = FirebaseAuth.getInstance();

        buttonDoctorLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openDoctorForm();
            } //Opens the doctor registration form.
        });

        buttonPatientLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openPatientForm();
            } //Opens the patient registration form.
        });

        welcomeLogin.setOnClickListener(new View.OnClickListener(){ //Arn use this! It's the login button functionality.
            @Override
            public void onClick(View view){
                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(MainActivity.this, "Authentication successful.",
                                            Toast.LENGTH_SHORT).show();
                                    openWelcomeScreen();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
    public void openDoctorForm(){
        Intent intent = new Intent(this, DoctorForm.class);
        startActivity(intent);
    }

    public void openPatientForm(){
        Intent intent = new Intent(this, PatientForm.class);
        startActivity(intent);
    }

    public void openWelcomeScreen(){
        Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
    }
}