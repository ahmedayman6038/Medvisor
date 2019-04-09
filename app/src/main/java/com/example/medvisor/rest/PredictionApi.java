package com.example.medvisor.rest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PredictionApi {
    @GET("/api/symptoms")
    Call<ArrayList<String>> getSymptom(@Query("Name") String name);
}
