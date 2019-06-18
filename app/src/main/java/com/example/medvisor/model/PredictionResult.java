package com.example.medvisor.model;

import java.util.ArrayList;

public class PredictionResult {
    private ArrayList<Disease> diseases;
    private ArrayList<Doctor> doctors;

    public PredictionResult(ArrayList<Disease> diseases, ArrayList<Doctor> doctors) {
        this.diseases = diseases;
        this.doctors = doctors;
    }

    public ArrayList<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(ArrayList<Disease> diseases) {
        this.diseases = diseases;
    }

    public ArrayList<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }
}
