package com.example.hams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
                        Toast.makeText(WelcomeScreenAdmin.this, "Selected User: "+ user.getFirstName(), Toast.LENGTH_SHORT).show();
                        approve.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                adapter.remove(user);
                                Map<String, Object> map= new HashMap<>();
                                map.put("status", User.APPROVED);
                                ref.child(user.getUserID()).updateChildren(map);
                            }
                        });

                        reject.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                adapter.remove(user);
                                Map<String, Object> map= new HashMap<>();
                                map.put("status", User.REJECTED);
                                ref.child(user.getUserID()).updateChildren(map);
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
                        Toast.makeText(WelcomeScreenAdmin.this, "Selected User: "+ user.getFirstName(), Toast.LENGTH_SHORT).show();
                        approveRejected.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                adapter.remove(user);
                                Map<String, Object> map= new HashMap<>();
                                map.put("status", User.APPROVED);
                                ref.child(user.getUserID()).updateChildren(map);
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
}