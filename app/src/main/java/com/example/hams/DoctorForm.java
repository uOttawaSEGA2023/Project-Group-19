package com.example.hams;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DoctorForm extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText phoneNumber;
    EditText address;
    EditText employeeNumber;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_form);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        employeeNumber = findViewById(R.id.employeeNumber);
        register = findViewById(R.id.registerDoctor);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Method that processes code when the register button is pressed.
                if (validFields()){ //If all the fields in the form are valid, do the following.

                }
            }
        });
    }

    /**
     * validFields is a method that checks to see if all the text fields have the correct information in order to register a doctor.
     * @return Whether or not every field in the form is valid.
     */
    boolean validFields(){
        boolean valid = true; //Variable to keep track of the current field validity.
        if (fieldEmpty(firstName)){  //If the first name field is empty, do the following.
            valid = false; //Set valid to false.
            firstName.setError("A first name is required to register!");
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
        if (fieldEmpty(address)){ //If the address field is empty, do the following.
            valid = false; //Set valid to false.
            address.setError("An address is required to register!"); //Tell the user to enter something into the field.
        }
        if (fieldEmpty(employeeNumber)){ //If the employee number field is empty, do the following.
            valid = false; //Set valid to false.
            employeeNumber.setError("An employee number is required to register!"); //Tell the user to enter something into the field.
        }


        CharSequence emailField = email.getText().toString(); //Gets the text from the email field.
        if (!Patterns.EMAIL_ADDRESS.matcher(emailField).matches()){ //If the email does not have the proper components of an email, do the following.
            valid = false; //Set valid to false.
            email.setError("A valid email needs to include proper components!"); //Tell the user to type an email with a proper format.
        }

        CharSequence phoneField = phoneNumber.getText().toString(); //Gets the text from the phone number field.
        if (phoneField.length() != 10){ //If the phone number is not equal to 10 digits, do the following.
            valid = false; //Set valid to false.
            phoneNumber.setError("A valid phone number need to be 10 digits long!"); //Tell the user to type in a phone number that is 10 digits long.
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

}