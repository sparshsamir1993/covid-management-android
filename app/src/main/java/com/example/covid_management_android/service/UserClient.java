package com.example.covid_management_android.service;

import com.example.covid_management_android.model.AuthToken;
import com.example.covid_management_android.model.Login;
import com.example.covid_management_android.model.Question;
import com.example.covid_management_android.model.QuestionOption;
import com.example.covid_management_android.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserClient {
    @POST("login")
    Call<AuthToken> login(@Body Login login);

    @POST("signup")
    Call<User> signup(@Body Login login);

    @PATCH("update")
    Call<User> update(@Header("authorization") String authToken, @Header("refresh-token") String refreshToken, @Body User user);

    @GET("questionOption")
    Call<List<QuestionOption>> fetchQuestions();

}
