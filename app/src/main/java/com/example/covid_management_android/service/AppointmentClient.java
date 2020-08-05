package com.example.covid_management_android.service;

import com.example.covid_management_android.model.appointments.Appointment;
import com.example.covid_management_android.model.appointments.Timeslot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AppointmentClient {

    @GET("slotlist")
    Call<List<Timeslot>> getAvailableTimeslots(@Header("authorization") String authToken, @Header("refresh-token") String refreshToken, @Query("selectedDate") Long selectedDay, @Query("hospitalId") int hospitalId);

    @GET("userAppointments")
    Call<List<Appointment>> getUserAppointments(@Header("authorization") String authToken, @Header("refresh-token") String refreshToken, @Query("userId") int userId);

    @POST("book")
    Call<Appointment> bookUserAppointment(@Header("authorization") String authToken, @Header("refresh-token") String refreshToken, @Body Appointment newAppointment);
}
