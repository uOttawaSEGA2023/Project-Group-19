package com.example.hams;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;

public class DoctorForm extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText phoneNumber;
    EditText employeeNumber;
    EditText addressLine;
    EditText postalCode;
    EditText country;
    EditText province;
    EditText city;
    CheckBox internalMedicine;
    CheckBox obstetrics;
    CheckBox familyMedicine;
    CheckBox gynecology;
    CheckBox pediatrics;
    CheckBox neurology;
    Button register;
    List<String> specialties;
    Doctor d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_form);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Doctor Registration Form");
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Finds the user information edit texts.
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);
        employeeNumber = findViewById(R.id.employeeNumber);
        //Finds the address information edit texts.
        addressLine = findViewById(R.id.addressLine);
        postalCode = findViewById(R.id.postalCode);
        country = findViewById(R.id.country);
        province = findViewById(R.id.province);
        city = findViewById(R.id.city);
        //Finds the specialties check boxes.
        internalMedicine = findViewById(R.id.internalMedicine);
        obstetrics = findViewById(R.id.obstetrics);
        familyMedicine = findViewById(R.id.familyMedicine);
        gynecology = findViewById(R.id.gynecology);
        pediatrics = findViewById(R.id.pediatrics);
        neurology = findViewById(R.id.neurology);
        //Finds the register button.
        register = findViewById(R.id.registerDoctor);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Method that processes code when the register button is pressed.
                if (validFields()){ //If all the fields in the form are valid, do the following.
                    String getFirstName = firstName.getText().toString();
                    String getLastName = lastName.getText().toString();
                    String getEmail = email.getText().toString();
                    String getPassword = password.getText().toString();
                    String getPhoneNumber = phoneNumber.getText().toString();
                    String getEmployeeNumber = employeeNumber.getText().toString();
                    String getAddressLine = addressLine.getText().toString();
                    String getPostalCode = postalCode.getText().toString();
                    String getCountry = country.getText().toString();
                    String getProvince = province.getText().toString();
                    String getCity = city.getText().toString();
                    if (internalMedicine.isChecked()){ //If this specialty box is checked off, add it to the list.
                        specialties.add("Internal Medicine");
                    }
                    if (obstetrics.isChecked()){ //If this specialty box is checked off, add it to the list.
                        specialties.add("Obstetrics");
                    }
                    if (familyMedicine.isChecked()){ //If this specialty box is checked off, add it to the list.
                        specialties.add("Family Medicine");
                    }
                    if (gynecology.isChecked()){ //If this specialty box is checked off, add it to the list.
                        specialties.add("Gynecology");
                    }
                    if (pediatrics.isChecked()){ //If this specialty box is checked off, add it to the list.
                        specialties.add("Pediatrics");
                    }
                    if (neurology.isChecked()){ //If this specialty box is checked off, add it to the list.
                        specialties.add("Neurology");
                    }
                    openLoginScreen(); //Bring the user back to the log in screen.
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

        if (fieldEmpty(employeeNumber)){ //If the employee number field is empty, do the following.
            valid = false; //Set valid to false.
            employeeNumber.setError("An employee number is required to register!"); //Tell the user to enter something into the field.
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

        if (!boxesChecked()){ //If no specialties are checked off, set valid to false.
            valid = false;
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
     * Checks to make sure if at least one specialty checkbox was checked off.
     * @return If at least checkbox was checked off.
     */
    boolean boxesChecked(){
        if(internalMedicine.isChecked() || obstetrics.isChecked() || familyMedicine.isChecked() || gynecology.isChecked() || pediatrics.isChecked() || neurology.isChecked()){
            return true;
        }else{
            Toast t = Toast.makeText(this, "You must select one or more specialties to register!", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
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