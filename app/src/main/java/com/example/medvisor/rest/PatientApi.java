package com.example.medvisor.rest;

import com.example.medvisor.model.*;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.*;

public interface PatientApi {
    @POST("/api/app/patientlogin")
    Call<Patient> login(@Body Login patient,@Query("type") String type);
    @POST("/api/patients")
    Call<Patient> register(@Body Patient patient);
    @GET("/api/patients/{patientId}")
    Call<Patient> GetPatient(@Path("patientId") int patientId);
    @PUT("/api/patients/UpdateProfile/{patientId}")
    Call<Patient> UpdatePatient(@Path("patientId") int patientId,@Body Patient patient);
    @POST("/api/feedbacks/send")
    Call<Mail> snedFeedback(@Body Mail mail);
    @PUT("/api/patients/ResetPassword")
    Call<Patient> resetPassword(@Body Patient patient);
    @GET("/api/patients/GetPredictionResults/{patientId}")
    Call<ArrayList<Assessment>> getPatientResults(@Path("patientId") int patientId);
}
