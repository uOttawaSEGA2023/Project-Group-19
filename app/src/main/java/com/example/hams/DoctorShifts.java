package com.example.hams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DoctorShifts extends AppCompatActivity {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    Query appointmentQuery;
    ValueEventListener appointmentListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_shifts);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("HAMS - Doctor Account: Shifts");
        actionBar.setDisplayHomeAsUpEnabled(true);
        String doctorUID = FirebaseAuth.getInstance().getCurrentUser().getUid();


        Button appointments = findViewById(R.id.backDoctor); //Button to return to the appointments screen.
        Button deleteShift = findViewById(R.id.deleteShift); //Button to delete a shift.
        Button addShift = findViewById(R.id.addShift); //Button to add a shift.

        EditText shiftDate = findViewById(R.id.editTextDate); //EditText for the date.
        EditText shiftStartTime = findViewById(R.id.editTextTime); //EditText for the start time.
        EditText shiftEndTime = findViewById(R.id.editTextTime2); //EditText for the end time.

        ArrayList<Shift> shiftList = new ArrayList<>();
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
                appointmentListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Appointment> appointmentList = new ArrayList<>();

                        // get all appointments
                        // if the appointments belong to a patient don't delete
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            Appointment appointment = userSnapshot.getValue(Appointment.class);

                            if(!appointment.getPatientUID().equals("")){
                                Toast.makeText(DoctorShifts.this, "A patient has booked an appointment in your shift!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            appointmentList.add(appointment);
                        }

                        // delete the shift
                        shiftList.remove(shift);
                        listView.setAdapter(new ShiftAdapter(DoctorShifts.this, shiftList));
                        ref.child("shifts").child(doctorUID).child(shift.getKey()).removeValue();

                        // delete all associated empty appointments
                        for (Appointment emptyAppointment : appointmentList){
                            ref.child("appointments").child(emptyAppointment.getKey()).removeValue();
                        }

                        //Toast.makeText(DoctorShifts.this, "Deleted Shift: " + shift.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                };
                //Toast.makeText(DoctorShifts.this, "Selected Shift: " + shift.toString(), Toast.LENGTH_SHORT).show();

                deleteShift.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // no available adapter object so we update the list and then create a new adapter to display for now
                        boolean delete = true;

                        appointmentQuery = ref.child("appointments").orderByChild("shiftID").equalTo(shift.getKey());
                        appointmentQuery.addValueEventListener(appointmentListener);
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

                        if (!conflicts && shiftInputs()) {
                            // we add the shifts under the doctor's UID so that shifts are easy to find based on the doctor user
                            // push will generate a unique key that we can use to store our shift
                            DatabaseReference newRef = ref.child("shifts").child(doctorUID).push();
                            // store the key in the object as well so that it is easy to update
                            String shiftID = newRef.getKey();
                            shiftToAdd.setKey(shiftID);
                            // add the shift to the database under the unique key
                            newRef.setValue(shiftToAdd);
                            // creates unclaimed appointments in the database based on the end and start times given
                            addAppointments(startTime, endTime, doctorUID, date, shiftID);

                            shiftDate.getText().clear(); //Clear the date text box.
                            shiftStartTime.getText().clear(); //Clear the start time text box.
                            shiftEndTime.getText().clear(); //Clear the end time text box.


                        } else {
                            Toast.makeText(DoctorShifts.this, "Error in Fields or Conflicts", Toast.LENGTH_SHORT).show();
                        }

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
        });
    }

    public void onStop() {
        if(appointmentQuery != null && appointmentListener != null){
            appointmentQuery.removeEventListener(appointmentListener);
        }

        super.onStop();
    }

    /**
     * Method to show the doctor appointment related information.
     */
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
            inputDate = dateFormat.parse(date); //Check to make sure the date input is of the correct format.


            String[] dateArr = date.split("-");

            if (Integer.parseInt(dateArr[1]) < 1 || Integer.parseInt(dateArr[1]) > 12) {

                textField.setError("Invalid date format for yyyy-MM-dd. Please make sure MM is between 1-12.");
                return false;
            }

            if (Integer.parseInt(dateArr[2]) < 1 || Integer.parseInt(dateArr[2]) > 30) {
                textField.setError("Invalid date format for yyyy-MM-dd. Please make sure day is between 1-31.");
                return false;
            }


        } catch (ParseException e) {
            textField.setError("Invalid date format for yyyy-MM-dd. Please use yyyy-MM-dd.");
            return false;
        }

        if (inputDate.before(currentDate)) { //If the date entered has passed already, make an error.
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
            start = timeFormat.parse(startTime); //Check to make sure the start time input is of correct format.
            ;
            if(!checkTimeBounds(startTime)) {
                startTimeField.setError("Invalid time format. Please input a time 0:00 - 23:59.");
                return false;
            }
        } catch (ParseException e) {
            startTimeField.setError("Invalid time format. Please use HH:mm.");
            return false;
        }
        try {
            end = timeFormat.parse(startTime); //Check to make sure the end time input is of correct format.
            if(!checkTimeBounds(endTime)) {
                endTimeField.setError("Invalid time format. Please input a time 0:00 - 23:59.");
                return false;
            }
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

    private boolean checkTimeBounds(String hour_min) {

       String[] timeArr = hour_min.split(":");
       if (Integer.parseInt(timeArr[0])< 0 || Integer.parseInt(timeArr[0]) > 24) {
           return false;
       }

        if (Integer.parseInt(timeArr[1]) < 0 || Integer.parseInt(timeArr[1]) > 59) {
            return false;
        }

        return true;
    }

    private boolean shiftInputs()  {
        boolean checked = true;
        EditText shiftDate = findViewById(R.id.editTextDate);
        EditText shiftStartTime = findViewById(R.id.editTextTime);
        EditText shiftEndTime = findViewById(R.id.editTextTime2);
        //Check to see if any of the text fields are empty.
        if(fieldEmpty(shiftDate)){
            checked = false;
            shiftDate.setError("Please enter a date!");
        }
        if(fieldEmpty(shiftStartTime)){
            checked = false;
            shiftStartTime.setError("Please enter a start time!");
        }
        if(fieldEmpty(shiftEndTime)){
            checked = false;
            shiftEndTime.setError("Please enter an end time!");
        }
        CharSequence date = shiftDate.getText().toString();
        if(!validateDate((String) date, shiftDate)){ //Validate the date.
            checked = false;
        }
        CharSequence start = shiftStartTime.getText().toString();
        CharSequence end = shiftEndTime.getText().toString();
        if(!validateTimeFormat((String) start, (String) end, shiftStartTime, shiftEndTime)){ //Validate the times.
            return false;
        }
        String check30Start = ((String) start).substring(start.length()-2);
        String check30End = ((String) end).substring(end.length()-2);
        //Check to make sure the start and end times of the shift are in 30 minute increments.
        if(!check30Start.equals("00") && !check30Start.equals("30")){
            checked = false;
            Toast.makeText(DoctorShifts.this, "Shift end and start times need to be in 30 minute increments!", Toast.LENGTH_SHORT).show();
        }
        if(!check30End.equals("00") && !check30End.equals("30")){
            checked = false;
            Toast.makeText(DoctorShifts.this, "Shift end and start times need to be in 30 minute increments!", Toast.LENGTH_SHORT).show();
        }

        return checked;
    }

    private void addAppointments(String startTime, String endTime, String doctorUID, String date, String shiftID) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Date start = timeFormat.parse(startTime);
        Date end = timeFormat.parse(endTime);

        long timeDiff = end.getTime() - start.getTime();
        long timeDiffMins = timeDiff / 60000;
        long numA = timeDiffMins / 30; //Calculate the number of appointment slots to make.

        for(int i = 0; i < numA; i++){ //Creates appointments slots with the given doctor UID, an empty patient UID, and the date and start time of the shift.
            Appointment appointment = new Appointment("", doctorUID, date, startTime, shiftID);
            startTime = incrementTime(startTime);
            DatabaseReference newRef = ref.child("appointments").push();
            // store the key in the object as well so that it is easy to update
            appointment.setKey(newRef.getKey());
            // add the shift to the database under the unique key
            newRef.setValue(appointment);
        }
    }

    public String incrementTime(String time){
        String[] splitTime = time.split(":");
        int hours = Integer.parseInt(splitTime[0]);

        if(splitTime[1].equals("30")){
            hours ++;
            splitTime[1] = "00";
        }
        else if (splitTime[1].equals("00")){
            splitTime[1] = "30";
        }

        return hours +":"+splitTime[1];
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