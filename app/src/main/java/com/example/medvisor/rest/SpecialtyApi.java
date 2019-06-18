package com.example.medvisor.rest;

import com.example.medvisor.model.Specialty;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpecialtyApi {
    @GET("/api/Specialties/list")
    Call<ArrayList<Specialty>> GetSpecialties();
}
