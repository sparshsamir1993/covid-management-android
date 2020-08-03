package com.example.covid_management_android.service;

import com.example.covid_management_android.model.appointments.Timeslot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface AppointmentClient {

    @GET("slotlist")
    Call<List<Timeslot>> getAvailableTimeslots(@Header("authorization") String authToken, @Header("refresh-token") String refreshToken,@Query("selectedDate") Long selectedDay, @Query("hospitalId") int hospitalId);
}
