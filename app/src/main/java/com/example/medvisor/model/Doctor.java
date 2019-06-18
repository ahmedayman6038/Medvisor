package com.example.medvisor.model;

import java.util.ArrayList;
import java.util.Date;

public class Doctor {
    private int id;
    private String name;
    private String email;
    private String password;
    private Date createdDate;
    private String city;
    private String address;
    private String information;
    private float rating;

    public Doctor(int id, String name, String email, String password, Date createdDate, String city, String address, String information) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
        this.city = city;
        this.address = address;
        this.information = information;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
