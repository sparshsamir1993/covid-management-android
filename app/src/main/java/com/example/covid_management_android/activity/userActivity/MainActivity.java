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
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.AppUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity implements PermissionListener{


    AppUtil appUtil = new AppUtil();
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

    private void requestForLocation() {
        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this).check();
    }

    private void fetchLocationData() {
      /*  final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(0)
                .setFastestInterval(0)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);*/

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        else{
          /*  LocationServices.getFusedLocationProviderClient(MainActivity.this)
                    .requestLocationUpdates(locationRequest,new LocationCallback(){
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                         Log.i("location size",String.valueOf(locationResult.getLocations().size()));
                            if(locationResult != null && locationResult.getLocations().size()>0){
                                Integer newLocation = locationResult.getLocations().size()-1;
                                Log.i("newlocation",newLocation.toString());
                                Double longi = locationResult.getLocations().get(newLocation).getLongitude();
                                Double lati = locationResult.getLocations().get(newLocation).getLatitude();

                                Log.i("Longitude",Double.toString(longi));
                                Log.i("Latitude",Double.toString(lati));

                            }
                        }
                    }, Looper.getMainLooper());*/

          fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
              @Override
              public void onSuccess(Location location) {
                  if(location!=null)
                  {
                      double longi = location.getLongitude();
                      double lati = location.getLatitude();

                    //  Log.i("Longitude",Double.toString(longi));
                      Log.i("Latitude",Double.toString(lati));

                      locationEditor.putFloat("Longitude",(float)longi);
                      locationEditor.putFloat("Latitude",(float)lati);
                      locationEditor.apply();

                  }
                  else
                  {
                      Log.i("LAt Long error","ERROORR");
                  }

              }
          });

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      locationPreferences = getSharedPreferences("covidManagement",MODE_PRIVATE);
      locationEditor = locationPreferences.edit();
      fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
      requestForLocation();
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        Toast.makeText(MainActivity.this, "Location Permission Granted", Toast.LENGTH_LONG).show();
        fetchLocationData();

        float longitude = locationPreferences.getFloat("Longitude",0);
        float latitude = locationPreferences.getFloat("Latitude",0);
        Log.i("LATI",String.valueOf(longitude));
        Log.i("Longi",String.valueOf(latitude));
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        if (permissionDeniedResponse.isPermanentlyDenied()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);

        } else {
            Toast.makeText(MainActivity.this, "Location Permission denied", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
             permissionToken.continuePermissionRequest();
    }
}