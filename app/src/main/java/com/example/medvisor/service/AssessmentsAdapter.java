package com.example.medvisor.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.medvisor.R;
import com.example.medvisor.model.Assessment;
import com.example.medvisor.model.Symptom;

import java.util.ArrayList;

public class AssessmentsAdapter extends ArrayAdapter<Assessment> {
    public AssessmentsAdapter(Context context, ArrayList<Assessment> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Assessment assessment = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_assessments, parent, false);
        }
        // Lookup view for data population
        TextView assessmentName = convertView.findViewById(R.id.assessmentName);
        TextView assessmentDate = convertView.findViewById(R.id.assessmentDate);
        // Populate the data into the template view using the data object
        assessmentName.setText(assessment.getName());
        assessmentDate.setText(assessment.getDate().toString());
        // Return the completed view to render on screen
        return convertView;
    }
}
