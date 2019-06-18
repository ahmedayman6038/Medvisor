package com.example.medvisor.service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.medvisor.R;
import com.example.medvisor.model.Symptom;

import java.util.ArrayList;

public class PredictionAdapter extends ArrayAdapter<Symptom> {
    public PredictionAdapter(Context context, ArrayList<Symptom> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Symptom symptom = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_symptoms_prediction, parent, false);
        }
        // Lookup view for data population
        TextView symptomName = convertView.findViewById(R.id.symptomName2);
        // Populate the data into the template view using the data object
        symptomName.setText(symptom.getName());
        // Return the completed view to render on screen
        Animation lefttori = AnimationUtils.loadAnimation(this.getContext(),R.anim.slide_in_left);
        convertView.startAnimation(lefttori);
        return convertView;
    }
}
