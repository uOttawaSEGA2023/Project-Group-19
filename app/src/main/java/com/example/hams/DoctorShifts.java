package com.example.hams;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DoctorShifts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_shifts);

        String doctorUID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Button appointments = findViewById(R.id.backDoctor);
        Button deleteShift = findViewById(R.id.deleteShift);
        Button addShift = findViewById(R.id.addShift);

        EditText shiftDate = findViewById(R.id.editTextDate);
        EditText shiftStartTime = findViewById(R.id.editTextTime);
        EditText shiftEndTime = findViewById(R.id.editTextTime2);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ArrayList<Shift> shiftList = new ArrayList<>();
        //ShiftAdapter adapter = new ShiftAdapter(DoctorShifts.this, shiftList);
        ListView listView = (ListView) findViewById(R.id.shiftsList);

        ref.child("shifts").child(doctorUID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                // Listview adapter is initialized at the very start and updated every time a new shift is added
                Shift shift = snapshot.getValue(Shift.class);
                shiftList.add(shift);

                ShiftAdapter adapter = new ShiftAdapter(DoctorShifts.this, shiftList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {}
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Code to run with a selected item from the shifts list.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Shift shift = (Shift) parent.getItemAtPosition(position);

                Toast.makeText(DoctorShifts.this, "Selected Shift: " + shift.toString(), Toast.LENGTH_SHORT).show();

                deleteShift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // no available adapter object so we update the list and then create a new adapter to display for now
                        shiftList.remove(shift);
                        listView.setAdapter(new ShiftAdapter(DoctorShifts.this, shiftList));
                        ref.child("shifts").child(doctorUID).child(shift.getKey()).removeValue();
                        Toast.makeText(DoctorShifts.this, "Deleted Shift: " + shift.toString(), Toast.LENGTH_SHORT).show();
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


        addShift.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Get shift object parameters from edit texts
                String date = shiftDate.getText().toString();
                String startTime = shiftStartTime.getText().toString();
                String endTime = shiftEndTime.getText().toString();

                // Create shift object and add it to the database if no conflicts
                Shift shiftToAdd = new Shift(startTime, endTime, date);

                if (validateDate(date, shiftDate) && validateTimeFormat(startTime, endTime, shiftStartTime, shiftEndTime)) {
                    try {
                        boolean conflicts = shiftConflicts(shiftToAdd,shiftList);

                        if (!conflicts) {
                            // we add the shfits under the doctor's UID so that shifts are easy to find based on the doctor user
                            // push will generate a unique key that we can use to store our shift
                            DatabaseReference newRef = ref.child("shifts").child(doctorUID).push();
                            // store the key in the object as well so that it is easy to update
                            shiftToAdd.setKey(newRef.getKey());
                            // add the shift to the database under the unique key
                            newRef.setValue(shiftToAdd);
                        } else {
                            Toast.makeText(DoctorShifts.this, "Error in fields or conflicts", Toast.LENGTH_SHORT).show();
                        }

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        });
    }

    public void openAppointmentsScreen() {
        Intent intent = new Intent(this, WelcomeScreenDoctor.class);
        startActivity(intent);
    }

    //Make sure the date entered is before current date
    private boolean validateDate(String date, EditText textField) {
        // Validate date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        Date inputDate;

        try {
            inputDate = dateFormat.parse(date);
        } catch (ParseException e) {
            textField.setError(" Invalid date format. Please use yyyy-MM-dd.");
            return false;
        }

        if (inputDate.before(currentDate)) {
            textField.setError("Error: Cannot enter a date that has already passed.");
            return false;
        }
        return true;
    }

    private boolean validateTimeFormat(String startTime, String endTime, EditText startTimeField, EditText endTimeField) {
        // Validate time format
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date start, end;

        try {
            start = timeFormat.parse(startTime);
        } catch (ParseException e) {
            startTimeField.setError("Invalid time format. Please use HH:mm.");
            return false;
        }
        try {
            end = timeFormat.parse(startTime);
        } catch (ParseException e) {
            endTimeField.setError("Invalid time format. Please use HH:mm.");
            return false;
        }
        return true;
    }

    //verify any shift conflicts
    private boolean shiftConflicts(Shift newShift, ArrayList<Shift> shifts) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Date newShiftDate = dateFormat.parse(newShift.getDate());
        Date newStartTime = timeFormat.parse(newShift.getStartTime());
        Date newEndTime = timeFormat.parse(newShift.getEndTime());
        Log.d("TAG", "shiftConflicts: past parse");
        //compare each old shift with new shift to be added.
        for (Shift shift : shifts) {
            //if they are on the same date and..
            if (newShiftDate.equals(dateFormat.parse(shift.getDate())) &&
                    //if new shift start time is later than old shifts start time one but ends before old shifts end time.
                    //handles cases where the new shift starts during the current shift.
                    ((newStartTime.compareTo(timeFormat.parse(shift.getStartTime())) >= 0 && newStartTime.before(timeFormat.parse(shift.getEndTime()))) ||
                            //or if the end time of the new shift is after the start time of the current shift and also equal to or earlier than the end time of the old shift
                            //handles cases where the new shift ends during the current shift.
                            (newEndTime.after(timeFormat.parse(shift.getStartTime())) && newEndTime.compareTo(timeFormat.parse(shift.getEndTime())) <= 0))) {
                return true;
            }
        }
        return false;
    }
}