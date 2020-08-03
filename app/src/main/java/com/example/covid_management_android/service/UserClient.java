package com.example.covid_management_android.service;

import com.example.covid_management_android.model.AuthToken;
import com.example.covid_management_android.model.CovidQuestionResult;
import com.example.covid_management_android.model.CovidStats;
import com.example.covid_management_android.model.CurrentUser;
import com.example.covid_management_android.model.HospitalData;
import com.example.covid_management_android.model.Login;

import com.example.covid_management_android.model.Question;
import com.example.covid_management_android.model.User;
import com.example.covid_management_android.model.UserAnswerResponse;
import com.example.covid_management_android.model.UserSubmission.UserSubmittedAnswers;

import org.json.JSONArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserClient {
    @POST("login")
    Call<AuthToken> login(@Body Login login);

    @POST("signup")
    Call<User> signup(@Body Login login);

    @PATCH("update")
    Call<User> update(@Header("authorization") String authToken, @Header("refresh-token") String refreshToken, @Body User user);

    @GET("options")
    Call<List<Question>> fetchQuestions(@Header("authorization") String authToken, @Header("refresh-token") String refreshToken);

    @POST("addResponse")
    Call<CovidQuestionResult> createReport(@Header("authorization") String authToken, @Header("refresh-token") String refreshToken,@Body UserAnswerResponse userAnswerResponse);

    @GET("getResponse")
    Call<List<UserSubmittedAnswers>> fetchData(@Header("authorization") String authToken, @Header("refresh-token") String refreshToken, @Query("userId") int userId);

    @PATCH("updateResponse")
    Call<CovidQuestionResult> updateUserQuestionnaire(@Header("authorization") String authToken, @Header("refresh-token") String refreshToken,@Body UserAnswerResponse userAnswerResponse);

    @GET("nearby")
    Call<List<HospitalData>> fetchHospitals(
            @Header("authorization") String authToken,
            @Header("refresh-token") String refreshToken,
            @Query("latitude") float latitude,
            @Query("longitude") float longitude
            );

    @GET("all")
    Call<CovidStats> fetchCovidStats();


}
