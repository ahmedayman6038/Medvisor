package com.example.medvisor.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.medvisor.R;
import com.example.medvisor.model.Doctor;
import com.example.medvisor.model.Symptom;

import java.util.ArrayList;

public class DoctorsAdapter extends ArrayAdapter<Doctor> {
    public DoctorsAdapter(Context context, ArrayList<Doctor> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Doctor doctor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_doctors_search, parent, false);
        }
        // Lookup view for data population
        TextView doctorName = convertView.findViewById(R.id.doctorName);
        TextView doctorCity = convertView.findViewById(R.id.doctorCity);
        TextView doctorRate = convertView.findViewById(R.id.doctorRate);
        // Populate the data into the template view using the data object
        doctorName.setText(doctor.getName());
        doctorCity.setText(doctor.getCity());
       // ratingBar.setRating(doctor.getRating());
        doctorRate.setText(String.valueOf((int)Math.floor(doctor.getRating())) + " / 5");
        // Return the completed view to render on screen
        return convertView;
    }
}
