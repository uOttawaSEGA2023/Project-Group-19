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


        //Authenticate user with firebase when they press the login button
        welcomeLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String email = emailLogin.getText().toString(); //Gets the email from the text field.
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


    /**
     * Opens a the home page for the designated type of user based on the log in details.
     */
    public void openWelcomeScreen(){
        String userId = mAuth.getUid();
        mDatabase.child("users").child(userId).child("type").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Once type is found update the welcome text field to indicate type
                String type = task.getResult().getValue(String.class);
                if(type.equals("admin")){ //If the user's type is admin, bring them to the admin home page.
                    Intent intent = new Intent(MainActivity.this, WelcomeScreenAdmin.class);
                    startActivity(intent);
                }else{
                    mDatabase.child("users").child(userId).child("status").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            //Once type is found update the welcome text field to indicate type
                            String status = task.getResult().getValue(String.class);
                            if(status.equals("pending")){ //If the user's status is pending, inform them that their form has not been processed yet.
                                Toast.makeText(MainActivity.this, "Your Registration Form has not been processed yet.", Toast.LENGTH_SHORT).show();
                            }else if(status.equals("rejected")){ //If the user's status is rejected, inform them to contact an admin.
                                Toast.makeText(MainActivity.this, "Your Registration Form was rejected. Please call the Administrator at 905-500-3000 to resolve this issue.", Toast.LENGTH_LONG).show();
                            }else{ //Otherwise, take the user to the correct welcome screen.
                                if(type.equals("doctor")){ //If the user's type is doctor, take them to the doctor welcome screen.
                                    Intent intent = new Intent(MainActivity.this, WelcomeScreenDoctor.class);
                                    startActivity(intent);
                                }else{ //Otherwise, take them to the patient welcome screen.
                                    Intent intent = new Intent(MainActivity.this, WelcomeScreen.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    });
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