package com.example.covid_management_android.service;

import com.example.covid_management_android.model.AuthToken;
import com.example.covid_management_android.model.Login;
import com.example.covid_management_android.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserClient {
    @POST("login")
    Call<AuthToken> login(@Body Login login);

    @PUT("update")
    Call<ResponseBody> update(@Header("authorization") String authToken);
}
