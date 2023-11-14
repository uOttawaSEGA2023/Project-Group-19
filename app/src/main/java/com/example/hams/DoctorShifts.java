package com.example.hams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DoctorShifts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_shifts);

        Button appointments = findViewById(R.id.backDoctor);
        Button deleteShift = findViewById(R.id.deleteShift);
        Button addShift = findViewById(R.id.addShift);

        EditText shiftDate = findViewById(R.id.editTextDate);
        EditText shiftStartTime = findViewById(R.id.editTextTime);
        EditText shiftEndTime = findViewById(R.id.editTextTime2);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("shifts");

        ArrayList<Shift> shiftList = new ArrayList<>();
        ShiftAdapter adapter = new ShiftAdapter(DoctorShifts.this, shiftList);
        ListView listView = (ListView) findViewById(R.id.shiftsList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Code to run with a selected item from the shifts list.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Shift shift = (Shift) parent.getItemAtPosition(position);

                Toast.makeText(DoctorShifts.this, "Selected Shift: "+ shift.toString(), Toast.LENGTH_SHORT).show();

                deleteShift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.remove(shift);
                        Toast.makeText(DoctorShifts.this, "Deleted Shift: "+ shift.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        appointments.setOnClickListener(new View.OnClickListener() {
            //Upon clicking the view appointments button, bring the doctor back to appointment related information.
            public void onClick(View view) {
                openAppointmentsScreen();
            }
        });

        addShift.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String doctorUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String date = shiftDate.getText().toString();
                String startTime = shiftStartTime.getText().toString();
                String endTime = shiftEndTime.getText().toString();

                Shift shiftToAdd = new Shift(startTime, endTime, date);

                String key = ref.push().getKey();

                Map<String,Object> update = new HashMap<>();
            }
        });

    }

    public void openAppointmentsScreen() {
        Intent intent = new Intent(this, WelcomeScreenDoctor.class);
        startActivity(intent);
    }



}