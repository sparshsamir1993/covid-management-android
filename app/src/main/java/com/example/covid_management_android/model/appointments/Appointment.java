package com.example.covid_management_android.model.appointments;

import com.example.covid_management_android.model.HospitalData;

import java.sql.Time;
import java.util.Date;

public class Appointment {

    int id;
    int userId;
    int hospitalId;
    java.util.Date appointmentDate;
    Long appointmentTime;
    String appointmentStatus;

    public HospitalData getHospital() {
        return hospital;
    }

    public void setHospital(HospitalData hospital) {
        this.hospital = hospital;
    }

    HospitalData hospital;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public java.util.Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Long getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Long appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }


}
