package com.example.medvisor.rest;

import com.example.medvisor.model.*;

import retrofit2.Call;
import retrofit2.http.*;

public interface PatientApi {
    @POST("/api/patients/login")
    Call<Patient> login(@Body Login patient);
}
