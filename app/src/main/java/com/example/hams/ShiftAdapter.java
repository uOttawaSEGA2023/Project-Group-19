package com.example.hams;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ShiftAdapter extends ArrayAdapter<Shift> {
    public ShiftAdapter(Context context, ArrayList<Shift> shifts) {
        super(context, 0, shifts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Shift s = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shifts, parent, false);
        }

        // Lookup view for data population
        TextView userName = (TextView) convertView.findViewById(R.id.shiftsDisplay);
        // Populate the data into the template view using the data object
        userName.setText(s.toString());
        // Return the completed view to render on screen
        return convertView;
    }
}
