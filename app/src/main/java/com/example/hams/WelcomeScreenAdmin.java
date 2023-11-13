package com.example.hams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class WelcomeScreenAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_admin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Admin Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button buttonLogin = findViewById(R.id.logoutAdmin); //logout button
        Button approve = findViewById(R.id.approveRequest); //approve button on the top
        Button reject = findViewById(R.id.rejectRequest); //reject button on the top
        Button approveRejected = findViewById(R.id.approveReject); //approve button on the bottom
        Button getInfo = findViewById(R.id.getInfoNew); //info button on the top
        Button getInfoReject = findViewById(R.id.getInfoReject); //info button on the bottom

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        // This query will return all users that have the pending status
        Query pendingQuery = ref.orderByChild("status").equalTo(User.PENDING);

        pendingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> userList = new ArrayList<>();

                // the children of snapshot represent our users
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user;
                    // get the type of the current user
                    String type = userSnapshot.child("type").getValue(String.class);

                    if (type.equals("patient")) { //If the user's type is patient, set user to the user from database as a patient.
                        user = userSnapshot.getValue(Patient.class);
                    } else { ///Otherwise, set user as a doctor.
                        user = userSnapshot.getValue(Doctor.class);
                    }
                    userList.add(user);
                }

                // the adapter will display the list of pending users in the top listview
                UsersAdapter adapter = new UsersAdapter(WelcomeScreenAdmin.this, userList);
                ListView listView = (ListView) findViewById(R.id.requests);
                listView.setAdapter(adapter);

                // when an item on the listview is clicked
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Code to run with a selected item from the registration requests list.
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        User user = (User) parent.getItemAtPosition(position);

                        Toast.makeText(WelcomeScreenAdmin.this, "Selected "+ user.toString(), Toast.LENGTH_SHORT).show();
                        approve.setOnClickListener(new View.OnClickListener(){ //If the approve button is pressed, do the following.

                            @Override
                            public void onClick(View view) {
                                // upon approval, stop displaying the approved user
                                adapter.remove(user);
                                // update the users status to approved
                                Map<String, Object> map = new HashMap<>();
                                map.put("status", User.APPROVED);
                                ref.child(user.getUserID()).updateChildren(map);
                                Toast.makeText(WelcomeScreenAdmin.this, "Approved: " + user.toString(), Toast.LENGTH_SHORT).show();
                                // allow the admin to send an email to the user
                                // the Subject:, To: and body areas should will all be filled in, the admin simply needs to press send
                                sendEmail(user.getUsername(), "Your registration has been approved!");
                            }
                        });

                        reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // when the user is rejected stop displaying them and update their status to rejected
                                adapter.remove(user);
                                Map<String, Object> map = new HashMap<>();
                                map.put("status", User.REJECTED);
                                ref.child(user.getUserID()).updateChildren(map);
                                Toast.makeText(WelcomeScreenAdmin.this, "Rejected: " + user.toString(), Toast.LENGTH_SHORT).show();
                                // send an email ot the user
                                sendEmail(user.getUsername(), "Your registration has been rejected!");
                            }
                        });

                        // displays user information when the getInfo button is pressed
                        getInfo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setContentView(R.layout.registration_info);

                                // setting title based on user type
                                if (user instanceof Doctor) {
                                    TextView title = findViewById(R.id.userRequest);
                                    title.setText("Doctor Form Submission");
                                } else {
                                    TextView title = findViewById(R.id.userRequest);
                                    title.setText("Patient Form Submission");
                                }

                                // dipslay all user common user parameters
                                TextView firstName = findViewById(R.id.firstNameList);
                                firstName.setText("First Name: " + user.getFirstName());

                                TextView lastName = findViewById(R.id.lastNameList);
                                lastName.setText("Last Name: " + user.getLastName());

                                TextView email = findViewById(R.id.emailList);
                                email.setText("Email Address: " + user.getUsername());

                                TextView phone = findViewById(R.id.phoneList);
                                phone.setText("Phone Number: " + user.getPhoneNumber());

                                TextView address = findViewById(R.id.addressList);
                                address.setText(user.getAddress().toString());

                                // if the user is a doctor display their employee number and specialties
                                if (user instanceof Doctor) {
                                    TextView employee = findViewById(R.id.numberList);
                                    employee.setText("Employee Number: " + ((Doctor) user).getEmployeeNumber());

                                    TextView special = findViewById(R.id.specialList);
                                    String display = "";
                                    ArrayList<String> list = ((Doctor) user).getSpecialties();
                                    for (int i = 0; i < list.size(); i++) {
                                        display = display + list.get(i) + ", ";
                                    }
                                    display = display.substring(0, display.length() - 2);
                                    special.setText("Specialties: " + display);
                                }
                                // otherwise the user is a patient and we display their healthcard number
                                else {
                                    TextView health = findViewById(R.id.numberList);
                                    health.setText("Health Card Number: " + ((Patient) user).getHealthCardNumber());
                                }

                                Toast.makeText(WelcomeScreenAdmin.this, "Opening Registration Form Information", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        // This query will return a snapshot of the rejected users
        Query rejectedQuery = ref.orderByChild("status").equalTo(User.REJECTED);

        rejectedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> userList = new ArrayList<>();
                // generate list of rejected users using the snapshot
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user;
                    String type = userSnapshot.child("type").getValue(String.class);

                    // get the object based on the user type flag
                    if (type.equals("patient")) {
                        user = userSnapshot.getValue(Patient.class);
                    } else {
                        user = userSnapshot.getValue(Doctor.class);
                    }
                    userList.add(user);
                }

                UsersAdapter adapter = new UsersAdapter(WelcomeScreenAdmin.this, userList);
                ListView listView = (ListView) findViewById(R.id.rejects);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        User user = (User) parent.getItemAtPosition(position);
                        Toast.makeText(WelcomeScreenAdmin.this, "Selected " + user.toString(), Toast.LENGTH_SHORT).show();
                        approveRejected.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Stop displaying user
                                adapter.remove(user);
                                // update status to approved
                                Map<String, Object> map = new HashMap<>();
                                map.put("status", User.APPROVED);
                                ref.child(user.getUserID()).updateChildren(map);
                                Toast.makeText(WelcomeScreenAdmin.this, "Approved: " + user.toString(), Toast.LENGTH_SHORT).show();
                                // send email to user
                                sendEmail(user.getUsername(), "Your registration has been approved!");
                            }
                        });

                        getInfoReject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setContentView(R.layout.registration_info);

                                // set title based on user type
                                if (user instanceof Doctor) {
                                    TextView title = findViewById(R.id.userRequest);
                                    title.setText("Doctor Form Submission");
                                } else {
                                    TextView title = findViewById(R.id.userRequest);
                                    title.setText("Patient Form Submission");
                                }

                                //display common attributes
                                TextView firstName = findViewById(R.id.firstNameList);
                                firstName.setText("First Name: " + user.getFirstName());

                                TextView lastName = findViewById(R.id.lastNameList);
                                lastName.setText("Last Name: " + user.getLastName());

                                TextView email = findViewById(R.id.emailList);
                                email.setText("Email Address: " + user.getUsername());

                                TextView phone = findViewById(R.id.phoneList);
                                phone.setText("Phone Number: " + user.getPhoneNumber());

                                TextView address = findViewById(R.id.addressList);
                                address.setText(user.getAddress().toString());

                                // if user is a doctor display employee number and specialties
                                if (user instanceof Doctor) {
                                    TextView employee = findViewById(R.id.numberList);
                                    employee.setText("Employee Number: " + ((Doctor) user).getEmployeeNumber());
                                    TextView special = findViewById(R.id.specialList);
                                    String display = "";
                                    ArrayList<String> list = ((Doctor) user).getSpecialties();
                                    for (int i = 0; i < list.size(); i++) {
                                        display = display + list.get(i) + ", ";
                                    }
                                    display = display.substring(0, display.length() - 2);
                                    special.setText("Specialties: " + display);
                                }
                                // otherwise user is a patient so display healthcard number
                                else {
                                    TextView health = findViewById(R.id.numberList);
                                    health.setText("Health Card Number: " + ((Patient) user).getHealthCardNumber());
                                }
                                Toast.makeText(WelcomeScreenAdmin.this, "Opening Registration Form Information", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view) {
                openLoginScreen();
            }
        });
    }

    /**
     * Opens the login screen.
     */
    public void openLoginScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * //Method to enable the back button on the registration information page to return to the request lists.
     *
     * @param item The item
     * @return If the return was successful.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), WelcomeScreenAdmin.class);
        startActivity(intent);
        return true;
    }

    /**
     * //Method to send email
     *
     * @param emailAddress Recipient email
     * @param bodyMessage  Message to send in email body.
     */
    private void sendEmail(String emailAddress, String bodyMessage) {
        try{
            String adminEmail = "geegees132@gmail.com";
            // App password generated by gmail
            String adminPassword = "xweswobgxwtgvvax";

            Properties smtpProperties = new Properties();

            // Gmail SMTP server requires authentication, email address, password and port 587 for TLS
            // smtp.gmail.com is the host name of the Gmail SMTP server
            smtpProperties.put("mail.smtp.host", "smtp.gmail.com");
            smtpProperties.put("mail.smtp.port", "587");
            smtpProperties.put("mail.smtp.starttls.enable", "true");
            smtpProperties.put("mail.smtp.auth", "true");

            // get session object that stores protocol properties and authentication object
            Session session = Session.getInstance(smtpProperties, new Authenticator(){
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // data holder for username and password
                    return new PasswordAuthentication(adminEmail, adminPassword);
                }
            });

            //Creating a MIME format message to store email attributes
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(adminEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
            message.setSubject("HAMS Registration Update");
            message.setText(bodyMessage);

            // creating a new thread will prevent the on click method from being slowed while
            // the email is sent in the background
            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            thread.start();
            Toast.makeText(WelcomeScreenAdmin.this, "Update sent", Toast.LENGTH_SHORT).show();
        } catch(MessagingException e){
            e.printStackTrace();
        }

        /*
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, "HAMS Registration Update");
        email.putExtra(Intent.EXTRA_TEXT, bodyMessage);

        //need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));

         */
    }
}

