package com.example.covid_management_android.activity.appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.AppUtil;
import com.example.covid_management_android.adapter.AppointmentAdapter;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.appointments.Appointment;
import com.example.covid_management_android.service.AppointmentClient;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.covid_management_android.constants.Constants.BASE_URL;
import static com.example.covid_management_android.constants.Constants.REFRESH_TOKEN;
import static com.example.covid_management_android.constants.Constants.SHARED_PREF_MAIN_NAME;
import static com.example.covid_management_android.constants.Constants.TOKEN;
import static com.example.covid_management_android.constants.Constants.USER_ID;

public class AppointmentHistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AppointmentAdapter.AppointmentCardListener {


    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    AppointmentClient appointmentClient;
    List<Appointment> appointmentList;
    SharedPreferences sharedPreferences;
    RecyclerView apRecyclerview;
    RecyclerView.LayoutManager apLayoutManager;
    AppUtil appUtil = new AppUtil();
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_history);
        sharedPreferences = getSharedPreferences(SHARED_PREF_MAIN_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString(TOKEN, null);
        String refreshToken = sharedPreferences.getString(REFRESH_TOKEN, null);
        int userId = sharedPreferences.getInt(USER_ID, -1);
        retrofitUtil = new RetrofitUtil(BASE_URL + "/appointment/");
        retrofit = retrofitUtil.getRetrofit();
        retrofitUtil.setContext(AppointmentHistoryActivity.this);
        appointmentClient = retrofit.create(AppointmentClient.class);
        apRecyclerview = findViewById(R.id.appointmentListRecycler);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.bringToFront();
        navigationView.requestLayout();
        appUtil.checkMenuItems(navigationView.getMenu(), AppointmentHistoryActivity.this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (token.split("JWT ").length == 1) {
            token = "JWT " + token;
        }

        if (token == null || refreshToken == null || userId < 1) {
            return;
        }
        Call<List<Appointment>> appointmentListCall = appointmentClient.getUserAppointments(token, refreshToken, userId);
        appointmentListCall.enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.isSuccessful()) {
                    appointmentList = response.body();
                    Log.i("Success", "  List is " + appointmentList.get(0).getAppointmentStatus());
                    AppointmentAdapter apAdapter = new AppointmentAdapter(AppointmentHistoryActivity.this, appointmentList, AppointmentHistoryActivity.this);
                    apLayoutManager = new LinearLayoutManager(AppointmentHistoryActivity.this);
                    apRecyclerview.setLayoutManager(apLayoutManager);
                    apRecyclerview.setAdapter(apAdapter);

                } else {
                    Log.i("Failed", "  to get list");
                }

            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void oncardClick(int position) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.i("nav item", " clicked");
        appUtil.createMenuItems(menuItem, AppointmentHistoryActivity.this, drawerLayout);
        return true;

    }
}