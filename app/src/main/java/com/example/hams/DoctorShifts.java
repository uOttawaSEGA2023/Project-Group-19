package com.example.hams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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



    }

    public void openAppointmentsScreen() {
        Intent intent = new Intent(this, WelcomeScreenDoctor.class);
        startActivity(intent);
    }



}