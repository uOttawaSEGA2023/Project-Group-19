package com.example.hams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class PatientForm extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText phoneNumber;
    EditText healthCardNumber;
    EditText addressLine;
    EditText postalCode;
    EditText country;
    EditText province;
    EditText city;
    Button register;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_form);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Patient Registration Form");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Instantiating objects needed for firebase storage and authentication
        mAuth = FirebaseAuth.getInstance();

        //Finds the user information edit texts.
        firstName = findViewById(R.id.firstNameP);
        lastName = findViewById(R.id.lastNameP);
        email = findViewById(R.id.emailP);
        password = findViewById(R.id.passwordP);
        phoneNumber = findViewById(R.id.phoneNumberP);
        healthCardNumber = findViewById(R.id.healthCard);

        //Finds the address information edit texts.
        addressLine = findViewById(R.id.addressLineP);
        postalCode = findViewById(R.id.postalCodeP);
        country = findViewById(R.id.countryP);
        province = findViewById(R.id.provinceP);
        city = findViewById(R.id.cityP);

        //Finds the register button.
        register = findViewById(R.id.registerPatient);
        Toast t = Toast.makeText(this, "Patient Account successfully created!", Toast.LENGTH_SHORT);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Method that processes code when the register button is pressed.
                if (validFields()){ //If all the fields in the form are valid, do the following.
                    //get all inputs from text fields as strings and create a patient object
                    String getFirstName = firstName.getText().toString();
                    String getLastName = lastName.getText().toString();
                    String getEmail = email.getText().toString();
                    String getPassword = password.getText().toString();
                    String getPhoneNumber = phoneNumber.getText().toString();
                    String getHealthCardNumber = healthCardNumber.getText().toString();

                    HashMap<String,String> address = new HashMap<> (5);
                    address.put("address line", addressLine.getText().toString());
                    address.put("postal code", postalCode.getText().toString());
                    address.put("country", country.getText().toString());
                    address.put("province", province.getText().toString());
                    address.put("city", city.getText().toString());

                    Patient user = new Patient(getFirstName, getLastName, getEmail, getPassword, getPhoneNumber, address, getHealthCardNumber);
                    DatabaseReference ref = database.getReference();

                    //creating a user on firebase and storing the patient's data
                    mAuth.createUserWithEmailAndPassword(getEmail, getPassword)
                            .addOnCompleteListener(PatientForm.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        //user successfully created
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        //Add the user to realtime database with a identifier for what type of user they are
                                        String userId = firebaseUser.getUid();
                                        ref.child("users").child(userId).setValue(user);
                                        ref.child("users").child(userId).child("type").setValue("patient");
                                        //Toast that lets user know that the registration was successful
                                        t.show();
                                        openLoginScreen(); //Bring the user back to the log in screen.

                                    } else {
                                        //Show a toast with error message if registration fails
                                        FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                        Toast.makeText(PatientForm.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    /**
     * validFields() will check every field with criteria to make sure the form is properly filled.
     * @return If every field in the form is valid or not.
     */
    boolean validFields(){
        boolean valid = true; //Variable to keep track of the current field validity.
        if (fieldEmpty(firstName)){  //If the first name field is empty, do the following.
            valid = false; //Set valid to false.
            firstName.setError("A first name is required to register!"); //Tell the user to enter something into the field.
        }
        if (fieldEmpty(lastName)){ //If the last name field is empty, do the following.
            valid = false; //Set valid to false.
            lastName.setError("A last name is required to register!"); //Tell the user to enter something into the field.
        }
        if (fieldEmpty(email)){ //If the email field is empty, do the following.
            valid = false; //Set valid to false.
            email.setError("An email is required to register!"); //Tell the user to enter something into the field.
        }
        if (fieldEmpty(password)){ //If the password field is empty, do the following.
            valid = false; //Set valid to false.
            password.setError("A password is required to register!"); //Tell the user to enter something into the field.
        }
        if (fieldEmpty(phoneNumber)){ //If the phone number field is empty, do the following.
            valid = false; //Set valid to false.
            phoneNumber.setError("A phone number is required to register!"); //Tell the user to enter something into the field.
        }

        if (fieldEmpty(healthCardNumber)){ //If the health card number field is empty, do the following.
            valid = false; //Set valid to false.
            healthCardNumber.setError("A health card number is required to register!"); //Tell the user to enter something into the field.
        }
        if (fieldEmpty(addressLine)){ //If the address line field is empty, do the following.
            valid = false; //Set valid to false.
            addressLine.setError("An address line is required to register!"); //Tell the user to enter something into the field.
        }
        if (fieldEmpty(postalCode)){ //If the postal code field is empty, do the following.
            valid = false; //Set valid to false.
            postalCode.setError("A postal code is required to register!"); //Tell the user to enter something into the field.
        }
        if (fieldEmpty(country)){ //If the country field is empty, do the following.
            valid = false; //Set valid to false.
            country.setError("A country is required to register!"); //Tell the user to enter something into the field.
        }
        if (fieldEmpty(province)){ //If the province field is empty, do the following.
            valid = false; //Set valid to false.
            province.setError("A province is required to register!"); //Tell the user to enter something into the field.
        }
        if (fieldEmpty(city)){ //If the city field is empty, do the following.
            valid = false; //Set valid to false.
            city.setError("A city is required to register!"); //Tell the user to enter something into the field.
        }

        CharSequence emailField = email.getText().toString(); //Gets the text from the email field.
        if (!Patterns.EMAIL_ADDRESS.matcher(emailField).matches()){ //If the email does not have the proper components of an email, do the following.
            valid = false; //Set valid to false.
            email.setError("A valid email needs to include proper components!"); //Tell the user to type an email with a proper format.
        }

        CharSequence phoneField = phoneNumber.getText().toString(); //Gets the text from the phone number field.
        int length = 10; //Assume the phone number field will be 10 digits long.
        for (int i = 0; i < phoneField.length(); i++){ //For loop to exclude - and spaces from the phone number field for checking.
            if (phoneField.charAt(i) == '-' || phoneField.charAt(i) == ' '){
                length--;
            }
        }
        if (phoneField.length() != 10){ //If the phone number is not equal to 10 digits, do the following.
            valid = false; //Set valid to false.
            phoneNumber.setError("A valid phone number need to be 10 digits long!"); //Tell the user to type in a phone number that is 10 digits long.
        }

        CharSequence passwordField = password.getText().toString(); //Gets the text from the password field.
        if (passwordField.length() < 6){ //If the password is less than 6 characters long, do the following.
            valid = false; //Set valid to false.
            password.setError("A valid password needs to be at least 6 characters long!"); //Tell the user to type in a password that is at least 6 characters long.
        }


        return valid; //Returns whether or not all fields are valid.
    }

    /**
     * fieldEmpty is a method that takes the contents of a text field and checks to see if the user left it empty.
     * @param text The text field being checked.
     * @return Whether or not the text field is empty.
     */
    boolean fieldEmpty(EditText text){
        CharSequence entry = text.getText().toString();
        return TextUtils.isEmpty(entry);
    }


    /**
     * //Method to enable the back button on the doctor form to return to the main menu.
     * @param item The item
     * @return If the return was successful.
     */
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        return true;
    }


    /**
     * openLoginScreen() takes the user back to the login screen when activated.
     */
    public void openLoginScreen(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}