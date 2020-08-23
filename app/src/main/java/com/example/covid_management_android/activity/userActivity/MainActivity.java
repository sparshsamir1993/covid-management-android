package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.AppUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {


    AppUtil appUtil;
    FusedLocationProviderClient fusedLocationProviderClient;
    SharedPreferences locationPreferences;
    SharedPreferences.Editor locationEditor;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation_menu, menu);
        menu = appUtil.checkMenuItems(menu, MainActivity.this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item = appUtil.createMenuItems(item, MainActivity.this);
        return super.onOptionsItemSelected(item);
    }

    public void goToLogin(View view) {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
    }

    public void goToSignUp(View view) {
        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationPreferences = getSharedPreferences("covidManagement",MODE_PRIVATE);
        locationEditor = locationPreferences.edit();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        appUtil = new AppUtil();
        appUtil.enableLocation(this);
      
    }

}