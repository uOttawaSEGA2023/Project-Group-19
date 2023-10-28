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

public class WelcomeScreenAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_admin);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Admin Account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Button buttonLogin = findViewById(R.id.logoutAdmin);
        Button approve = findViewById(R.id.approveRequest);
        Button reject = findViewById(R.id.rejectRequest);
        Button approveRejected = findViewById(R.id.approveReject);
        Button getInfo = findViewById(R.id.getInfoNew);
        Button getInfoReject = findViewById(R.id.getInfoReject);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        Query pendingQuery = ref.orderByChild("status").equalTo(User.PENDING);

        pendingQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> userList = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user;
                    String type = userSnapshot.child("type").getValue(String.class);

                    if (type.equals("patient")) {
                        user = userSnapshot.getValue(Patient.class);
                    } else {
                        user = userSnapshot.getValue(Doctor.class);
                    }
                    userList.add(user);
                }

                UsersAdapter adapter = new UsersAdapter(WelcomeScreenAdmin.this, userList);
                ListView listView = (ListView) findViewById(R.id.requests);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        User user = (User) parent.getItemAtPosition(position);
                        Toast.makeText(WelcomeScreenAdmin.this, "Selected "+ user.toString(), Toast.LENGTH_SHORT).show();
                        approve.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                adapter.remove(user);
                                Map<String, Object> map= new HashMap<>();
                                map.put("status", User.APPROVED);
                                ref.child(user.getUserID()).updateChildren(map);
                                Toast.makeText(WelcomeScreenAdmin.this, "Approved: " + user.toString(), Toast.LENGTH_SHORT).show();
                                sendEmail(user.getFirstName(), "Your registration has been approved!");
                            }
                        });

                        reject.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                adapter.remove(user);
                                Map<String, Object> map= new HashMap<>();
                                map.put("status", User.REJECTED);
                                ref.child(user.getUserID()).updateChildren(map);
                                Toast.makeText(WelcomeScreenAdmin.this, "Rejected: " + user.toString(), Toast.LENGTH_SHORT).show();
                                sendEmail(user.getFirstName(), "Your registration has been rejected!");
                            }
                        });

                        getInfo.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                setContentView(R.layout.registration_info);
                                if(user instanceof Doctor){
                                    TextView title = findViewById(R.id.userRequest);
                                    title.setText("Doctor Form Submission");
                                }else{
                                    TextView title = findViewById(R.id.userRequest);
                                    title.setText("Patient Form Submission");
                                }
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
                                if(user instanceof Doctor){
                                    TextView employee = findViewById(R.id.numberList);
                                    employee.setText("Employee Number: " + ((Doctor) user).getEmployeeNumber());
                                    TextView special = findViewById(R.id.specialList);
                                    String display = "";
                                    ArrayList<String> list = ((Doctor) user).getSpecialties();
                                    for(int i = 0; i < list.size(); i++){
                                        display = display + list.get(i) + ", ";
                                    }
                                    display = display.substring(0, display.length() - 2);
                                    special.setText("Specialties: "+ display);
                                }else{
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
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        Query rejectedQuery = ref.orderByChild("status").equalTo(User.REJECTED);

        rejectedQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> userList = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user;
                    String type = userSnapshot.child("type").getValue(String.class);

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
                        Toast.makeText(WelcomeScreenAdmin.this, "Selected "+ user.toString(), Toast.LENGTH_SHORT).show();
                        approveRejected.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                adapter.remove(user);
                                Map<String, Object> map= new HashMap<>();
                                map.put("status", User.APPROVED);
                                ref.child(user.getUserID()).updateChildren(map);
                                Toast.makeText(WelcomeScreenAdmin.this, "Approved: " + user.toString(), Toast.LENGTH_SHORT).show();
                                sendEmail(user.getFirstName(), "Your registration has been approved!");
                            }
                        });

                        getInfoReject.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                setContentView(R.layout.registration_info);
                                if(user instanceof Doctor){
                                    TextView title = findViewById(R.id.userRequest);
                                    title.setText("Doctor Form Submission");
                                }else{
                                    TextView title = findViewById(R.id.userRequest);
                                    title.setText("Patient Form Submission");
                                }
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
                                if(user instanceof Doctor){
                                    TextView employee = findViewById(R.id.numberList);
                                    employee.setText("Employee Number: " + ((Doctor) user).getEmployeeNumber());
                                    TextView special = findViewById(R.id.specialList);
                                    String display = "";
                                    ArrayList<String> list = ((Doctor) user).getSpecialties();
                                    for(int i = 0; i < list.size(); i++){
                                        display = display + list.get(i) + ", ";
                                    }
                                    display = display.substring(0, display.length() - 2);
                                    special.setText("Specialties: "+ display);
                                }else{
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


        buttonLogin.setOnClickListener(new View.OnClickListener(){
            //Upon clicking the logout button sign user out and return to the login screen
            public void onClick(View view){
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

    /**
     * //Method to enable the back button on the registration information page to return to the request lists.
     * @param item The item
     * @return If the return was successful.
     */
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), WelcomeScreenAdmin.class);
        startActivity(intent);
        return true;
    }

    /**
     * //Method to send email
     * @param emailAddress Recipient email
     * @param bodyMessage Message to send in email body.
     */
    private void sendEmail(String emailAddress, String bodyMessage) {
        String recipientEmail = emailAddress;
        String subject = "HAMS Registration Update";
        String message = bodyMessage;

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + recipientEmail));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        startActivity(intent);
        //Add a Toast message to inform the user
        Toast.makeText(this, "Email sent to " + emailAddress, Toast.LENGTH_SHORT).show();
    }
}