package com.example.covid_management_android.model;


public class User {
    private int id;
    public void setId(int id) {
        this.id = id;
    }
    private String name;
    private  String dateOfBirth;
    private String email;
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getDateOfBirth() {
        return dateOfBirth;
    }
}
