package com.example.hams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText emailLogin;
    private EditText passwordLogin;
    FirebaseAuth mAuth = FirebaseAuth.getInstance(); //needed for authentication
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    String userId = mAuth.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Set top bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Login Page");

        Button welcomeLogin = findViewById(R.id.login); //Login button
        //Doctor and patient registration buttons
        Button buttonDoctorLogin = findViewById(R.id.registerDoctorLogin);
        Button buttonPatientLogin = findViewById(R.id.registerPatientLogin);
        Button buttonAdminLogin = findViewById(R.id.buttonAdmin);

        //find email and password inputs from login screen
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);


        /**
         * Button method for doctor registration form.
         */
        buttonDoctorLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openDoctorForm();
            } //Opens the doctor registration form.
        });
        /**
         * Button method for patient registration form.
         */
        buttonPatientLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openPatientForm();
            } //Opens the patient registration form.
        });

        buttonAdminLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                openAdmin();
            }
        });

        //Authenticate user with firebase when they press the login button
        welcomeLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();


                if(checkLogin()){
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        openWelcomeScreen();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }



            }
        });

    }

    /**
     * Opens the doctor registration form screen.
     */
    public void openDoctorForm(){
        Intent intent = new Intent(this, DoctorForm.class);
        startActivity(intent);
    }

    /**
     * Opens the patient registration form screen.
     */
    public void openPatientForm(){
        Intent intent = new Intent(this, PatientForm.class);
        startActivity(intent);
    }

    public void openAdmin(){
        Intent intent = new Intent(this, WelcomeScreenAdmin.class);
        startActivity(intent);
    }

    /**
     * Opens the home page welcome screen.
     */
    public void openWelcomeScreen(){

        mDatabase.child("users").child(userId).child("type").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Once type is found update the welcome text field to indicate type
                String type = task.getResult().getValue(String.class);
                if(type.equals("admin")){
                    Intent intent = new Intent(MainActivity.this, WelcomeScreenAdmin.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.this, WelcomeScreen.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Checks to make sure if the login text fields are not empty.
     * @return If the fields are empty or not.
     */
    boolean checkLogin(){
        boolean valid = true;
        CharSequence emailField = emailLogin.getText().toString();
        CharSequence pwField = passwordLogin.getText().toString();
        if(TextUtils.isEmpty(emailField)){
            emailLogin.setError("Enter a valid Email Address!");
            valid = false;
        }
        if(TextUtils.isEmpty(pwField)){
            passwordLogin.setError("Enter a valid password!");
            valid = false;
        }
        return valid;
    }
}