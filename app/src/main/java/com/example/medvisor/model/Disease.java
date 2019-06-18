package com.example.medvisor.model;

public class Disease {
    private int id;
    private String name;
    private String details;
    private Specialty specialty;

    public Disease(int id, String name, String details, Specialty specialty) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.specialty = specialty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }
}
