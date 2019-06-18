package com.example.medvisor.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.medvisor.R;
import com.example.medvisor.model.Specialty;

import java.util.ArrayList;

public class SpecialtiesAdapter extends ArrayAdapter<Specialty> {
    public SpecialtiesAdapter(Context context, ArrayList<Specialty> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Specialty specialty = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_specialties, parent, false);
        }
        // Lookup view for data population
        TextView specialtyName = convertView.findViewById(R.id.specialtyName);
        TextView specialtyDesc = convertView.findViewById(R.id.specialtyDesc);
        // Populate the data into the template view using the data object
        specialtyName.setText(specialty.getName());
        specialtyDesc.setText(specialty.getDetails());
        // Return the completed view to render on screen
        return convertView;
    }
}
