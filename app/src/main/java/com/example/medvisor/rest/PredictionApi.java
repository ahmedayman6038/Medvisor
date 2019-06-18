package com.example.medvisor.rest;

import com.example.medvisor.model.Assessment;
import com.example.medvisor.model.Mail;
import com.example.medvisor.model.Patient;
import com.example.medvisor.model.PredictionResult;
import com.example.medvisor.model.Symptom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PredictionApi {
    @GET("/api/symptoms")
    Call<ArrayList<Symptom>> getSymptom(@Query("Name") String name);
    @GET("/api/app/InitializePrediction/{symptomId}")
    Call<Integer> initializePrediction(@Path("symptomId") int symptomId);
    @GET("/api/app/Predict/{symptomId}")
    Call<ArrayList<Symptom>> startPrediction(@Path("symptomId") int symptomId);
    @GET("/api/app/EndPrediction/{patientId}")
    Call<Assessment> endPrediction(@Path("patientId") int patientId);
    @GET("/api/app/GetPredictionResult/{assessmentId}")
    Call<PredictionResult> getPredictionResult(@Path("assessmentId") int assessmentId);
    @DELETE("/api/app/DeletePredictionResult/{assessmentId}")
    Call<Assessment> deletePredictionResult(@Path("assessmentId") int assessmentId);

}
