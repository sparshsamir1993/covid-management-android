package com.example.covid_management_android.activity.appointments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.covid_management_android.R;
import com.example.covid_management_android.activity.userActivity.CovidQuestionnaireRedirection;
import com.example.covid_management_android.adapter.AppUtil;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;



public class AppointmentBookingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
   // HorizontalCalendar calender;
    Calendar startDate, endDate;
    AppUtil appUtil = new AppUtil();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking);
        startDate = Calendar.getInstance();
        startDate.add(Calendar.DATE, 0);

        endDate = Calendar.getInstance();
        endDate.add(Calendar.DATE, 14);


      /*  calender = new HorizontalCalendar.Builder(this, R.id.calendarView)

                .range(startDate, endDate)
                .datesNumberOnScreen(4)
                .mode(HorizontalCalendar.Mode.DAYS)
                .build();*/
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("");
//        setSupportActionBar(toolbar);
//
//        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
//        NavigationView navigationView = findViewById(R.id.navigationView);
////        navigationView.bringToFront();
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//
//        appUtil.checkMenuItems(navigationView.getMenu(), AppointmentBookingActivity.this);
//        navigationView.setNavigationItemSelectedListener( this);


     /*   calender.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                Log.i("position --- ", position+"");
            }
        });*/


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        appUtil.createMenuItems(menuItem, AppointmentBookingActivity.this);
        return true;
    }
}

