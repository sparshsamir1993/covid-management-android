package com.example.covid_management_android.activity.appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.AppUtil;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.constants.Constants;
import com.example.covid_management_android.model.appointments.Appointment;
import com.example.covid_management_android.service.AppointmentClient;
import com.google.android.material.navigation.NavigationView;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.covid_management_android.constants.Constants.APPOINTMENT_CONFIRMED;
import static com.example.covid_management_android.constants.Constants.APPOINTMENT_ID;
import static com.example.covid_management_android.constants.Constants.BASE_URL;
import static com.example.covid_management_android.constants.Constants.REFRESH_TOKEN;
import static com.example.covid_management_android.constants.Constants.SHARED_PREF_MAIN_NAME;
import static com.example.covid_management_android.constants.Constants.TEST_RESULTS_DELIVERED;
import static com.example.covid_management_android.constants.Constants.TEST_RESULT_PENDING;
import static com.example.covid_management_android.constants.Constants.TOKEN;

public class AppointmentDetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    //    String[] descriptionData = {"Details", "Status", "Photo", "Confirm"};
    JSONObject statusToProgressState;
    Retrofit retrofit;
    RetrofitUtil retrofitUtil;
    AppointmentClient appointmentClient;
    AppUtil appUtil;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SharedPreferences sharedPreferences;
    TextView hospitalNameText, appointmentDateTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        try {
            sharedPreferences = getSharedPreferences(SHARED_PREF_MAIN_NAME, MODE_PRIVATE);
            String token = sharedPreferences.getString(TOKEN, null);
            String refreshToken = sharedPreferences.getString(REFRESH_TOKEN, null);
            statusToProgressState = new JSONObject();
            statusToProgressState.put(APPOINTMENT_CONFIRMED, StateProgressBar.StateNumber.ONE);
            statusToProgressState.put(TEST_RESULT_PENDING, StateProgressBar.StateNumber.TWO);
            statusToProgressState.put(TEST_RESULTS_DELIVERED, StateProgressBar.StateNumber.THREE);
            String[] statusData = {stringWithReturn(APPOINTMENT_CONFIRMED), stringWithReturn(TEST_RESULT_PENDING), stringWithReturn(TEST_RESULTS_DELIVERED)};
            retrofitUtil = new RetrofitUtil(BASE_URL + "/appointment/");
            retrofit = retrofitUtil.getRetrofit();
            retrofitUtil.setContext(AppointmentDetailActivity.this);
            appointmentClient = retrofit.create(AppointmentClient.class);
            appUtil = new AppUtil();
            int appointmentId = getIntent().getIntExtra(APPOINTMENT_ID, -1);
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            drawerLayout = findViewById(R.id.drawerLayout);
            navigationView = findViewById(R.id.navigationView);
            navigationView.bringToFront();
            navigationView.requestLayout();
            appUtil.checkMenuItems(navigationView.getMenu(), AppointmentDetailActivity.this);
            navigationView.setNavigationItemSelectedListener(this);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            hospitalNameText = findViewById(R.id.hospitalNameText);
            appointmentDateTimeText = findViewById(R.id.appointmentDateTimeText);
            final StateProgressBar stateProgressBar = findViewById(R.id.appointmentStatusBar);
            stateProgressBar.setStateDescriptionData(statusData);
            if (token.split("JWT ").length == 1) {
                token = "JWT " + token;
            }

            if (appointmentId > 0) {

                Call<Appointment> appointmentCall = appointmentClient.getUserAppointmentDetail(token, refreshToken, appointmentId);
                appointmentCall.enqueue(new Callback<Appointment>() {
                    @Override
                    public void onResponse(Call<Appointment> call, Response<Appointment> response) {
                        if (response.isSuccessful()) {
                            Appointment currentApp = response.body();
                            try {
                                Log.i("state is", currentApp.getHospital().getName());
                                stateProgressBar.setCurrentStateNumber((StateProgressBar.StateNumber) statusToProgressState.get(currentApp.getAppointmentStatus()));
                                hospitalNameText.setText(currentApp.getHospital().getName());
                                String apTime = appUtil.getTimeStringFromSlot(currentApp.getAppointmentTime());
                                String apDate = appUtil.getDateStringFromDate(currentApp.getAppointmentDate());

                                appointmentDateTimeText.setText(apTime+", "+apDate);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Appointment> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private String stringWithReturn(String string) {
        String newString = "";
        String[] splitString = string.split(" ");
        if (splitString.length > 1) {
            for (int i = 0; i < splitString.length; i++) {
                newString += splitString[i] + "\n";
            }
        } else {
            return splitString[0];
        }
        return newString;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        appUtil.createMenuItems(menuItem, AppointmentDetailActivity.this, drawerLayout);
        return true;
    }
}