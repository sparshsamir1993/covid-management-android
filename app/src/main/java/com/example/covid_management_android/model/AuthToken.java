package com.example.covid_management_android.model;

import com.google.gson.annotations.SerializedName;

public class AuthToken {
    private String token;
    private String refreshToken;
    private Boolean auth;
    public Integer getId() {
        return id;
    }
    private Integer id;
    public String getToken() {
        return token;
    }
    public String getRefreshToken() {
        return refreshToken;
    }
    public Boolean getAuth() {
        return auth;
    }
}
