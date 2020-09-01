package com.example.covid_management_android.activity.appointments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.HospitalData;
import com.example.covid_management_android.model.appointments.Appointment;
import com.example.covid_management_android.service.AppointmentClient;

import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.covid_management_android.constants.Constants.BASE_URL;
import static com.example.covid_management_android.constants.Constants.HOSPITAL_DATA;
import static com.example.covid_management_android.constants.Constants.REFRESH_TOKEN;
import static com.example.covid_management_android.constants.Constants.SHARED_PREF_MAIN_NAME;
import static com.example.covid_management_android.constants.Constants.TOKEN;
import static com.example.covid_management_android.constants.Constants.USER_EMAIL;
import static com.example.covid_management_android.constants.Constants.USER_ID;

public class AppointmentConfirmationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView emailVal, slotVal, hospitalValue;
    JSONObject currHospitalData;
    Button confirmBookingBtn;
    java.util.Date selectedDate;
    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    AppointmentClient appointmentClient;
    Long selectedSlot;
    Toolbar toolbar;


    private void bookNewAppointment() {
        String token = sharedPreferences.getString(TOKEN, null);
        String refreshToken = sharedPreferences.getString(REFRESH_TOKEN, null);
        int userId = sharedPreferences.getInt(USER_ID, -1);

        Appointment appointmentData = new Appointment();
        DateFormat slotFormat = new SimpleDateFormat("HH");
        if (token != null && refreshToken != null && userId > 0) {
            try {
//                Time appointmentTime = new Time(slotFormat.parse(selectedSlot + "").getTime());
                Time appointmentTime= Time.valueOf(selectedSlot+":00:00");
//                t.setTime(selectedSlot);
//                Time appointmentTime = new Time(Integer.parseInt(selectedSlot+""),0,0);
//                appointmentTime.setTime(selectedSlot);
                int hospitalId = currHospitalData.getInt("hospitalId");
                appointmentData.setAppointmentTime(selectedSlot);
                appointmentData.setAppointmentDate(selectedDate);
                appointmentData.setHospitalId(hospitalId);
                appointmentData.setUserId(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Call<Appointment> newAppointmentCall;
            newAppointmentCall = appointmentClient.bookUserAppointment(token, refreshToken, appointmentData);
            newAppointmentCall.enqueue(new Callback<Appointment>() {
                @Override
                public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                    if(response.isSuccessful()){
                        Appointment newAppointment = response.body();
                        Log.i("New Appointment is ", newAppointment.getAppointmentStatus());

                        Toast.makeText(AppointmentConfirmationActivity.this, "Appointment Booked", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(AppointmentConfirmationActivity.this, AppointmentHistoryActivity.class);
                        startActivity(i);
                    }else{
                        Log.i("oops", " not success");
                    }
                }

                @Override
                public void onFailure(Call<Appointment> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_confirmation);

        emailVal = findViewById(R.id.emailValue);
        slotVal = findViewById(R.id.slotValue);
        hospitalValue = findViewById(R.id.hospitalValue);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Confirm Appointment");
        toolbar.hideOverflowMenu();
        toolbar.setNavigationIcon(R.drawable.nav_back_button);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        selectedSlot = getIntent().getLongExtra("selectedSlot", 0);
        Long date = getIntent().getLongExtra("selectedDate", -1);
        selectedDate = new Date(date);
        final String dateString = new SimpleDateFormat("yyyy-MMMM-dd").format(selectedDate);
        Log.i("hhhh -- ", date.toString());
        sharedPreferences = getSharedPreferences(SHARED_PREF_MAIN_NAME, MODE_PRIVATE);
        String email = sharedPreferences.getString(USER_EMAIL, null);
        String hospDataString = getIntent().getStringExtra(HOSPITAL_DATA);
        Long dateLong = getIntent().getLongExtra("selectedDate", -1);
        confirmBookingBtn = findViewById(R.id.confirmBookingBtn);
        retrofitUtil = new RetrofitUtil(BASE_URL + "/appointment/");
        retrofit = retrofitUtil.getRetrofit();
        retrofitUtil.setContext(AppointmentConfirmationActivity.this);
        appointmentClient = retrofit.create(AppointmentClient.class);


        confirmBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentConfirmationActivity.this);
                builder.setTitle("About to confirm Appointment");
                builder.setMessage("Appointment to be set at \n" + slotVal.getText());
                builder.setPositiveButton("Book !", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bookNewAppointment();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        try {
            currHospitalData = new JSONObject(hospDataString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (email != null) {
            Log.i("email in sp -- ", email + " " + selectedSlot);
            emailVal.setText(email);
            String slotText = "";
            if (selectedSlot < 12) {
                slotText = selectedSlot + ":00 am, " + dateString;
            } else if (selectedSlot == 12) {
                slotText = selectedSlot + ":00 pm, " + dateString;
            } else {
                slotText = (selectedSlot - 12) + ":00 pm, " + dateString;
            }
            slotVal.setText(slotText);
            try {

                hospitalValue.setText(currHospitalData.getString("hospitalAddress"));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}