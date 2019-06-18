package com.example.medvisor.rest;

import com.example.medvisor.model.Doctor;
import com.example.medvisor.model.Symptom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DoctorApi {
    @GET("/api/doctors")
    Call<ArrayList<Doctor>> getDoctors(@Query("Name") String name);
    @GET("/api/doctors/GetDoctorsBySpecialties/{specialtyId}")
    Call<ArrayList<Doctor>> getDoctorsBySpecialties(@Path("specialtyId") int specialtyId);
    @GET("/api/doctors/GetDoctorProfile/{doctorId}")
    Call<Doctor> getDoctorProfile(@Path("doctorId") int doctorId);
    @POST("/api/doctors/Rate/{doctorId}/{patientId}")
    Call<Object> rateDoctor(@Path("doctorId") int doctorId,@Path("patientId") int patientId,@Query("value") int value);
    @GET("/api/doctors/GetRate/{doctorId}/{patientId}")
    Call<Integer> GetRate(@Path("doctorId") int doctorId,@Path("patientId") int patientId);
}
