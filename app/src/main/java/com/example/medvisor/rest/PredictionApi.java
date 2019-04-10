package com.example.medvisor.rest;

import com.example.medvisor.model.Symptom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PredictionApi {
    @GET("/api/symptoms")
    Call<ArrayList<String>> getSymptom(@Query("Name") String name);
    @GET("/api/app/InitializePrediction")
    Call<Symptom> initializePrediction(@Query("Name") String name);
    @GET("/api/app/StartPrediction/{symptomId}")
    Call<ArrayList<Symptom>> startPrediction(@Path("symptomId") int symptomId);
}
