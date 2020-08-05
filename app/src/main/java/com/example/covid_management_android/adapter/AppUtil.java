package com.example.covid_management_android.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.covid_management_android.R;
import com.example.covid_management_android.activity.userActivity.CovidQuestionnaireRedirection;
import com.example.covid_management_android.activity.userActivity.HospitalList;
import com.example.covid_management_android.activity.userActivity.LoginActivity;
import com.example.covid_management_android.activity.userActivity.MainActivity;
import com.example.covid_management_android.activity.userActivity.NationalCovidStats;
import com.example.covid_management_android.activity.userActivity.QuestionActivity;
import com.example.covid_management_android.activity.userActivity.SignUpActivity;
import com.example.covid_management_android.activity.userActivity.UserProfileActivity;
import com.example.covid_management_android.model.CurrentUser;
import com.example.covid_management_android.model.UserSubmission.UserSubmittedAnswers;
import com.example.covid_management_android.service.UserClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppUtil extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Menu currentMenu;
    SharedPreferences locationPreferences;
    SharedPreferences.Editor locationEditor;
    FusedLocationProviderClient fusedLocationProviderClient;


    public Menu checkMenuItems(Menu menu, Context context) {
        sharedPreferences = context.getSharedPreferences("covidManagement", MODE_PRIVATE);
        currentMenu = menu;

        MenuItem loginItem = menu.findItem(R.id.login);
        MenuItem regItem = menu.findItem(R.id.register);
        MenuItem logoutItem = menu.findItem(R.id.logout);
        MenuItem profileItem = menu.findItem(R.id.profile);
        MenuItem hospitalListItem = menu.findItem(R.id.hospitalList);
        MenuItem questionListItem = menu.findItem(R.id.questionList);


        String token = sharedPreferences.getString("token", null);
        if (token != null) {                      // when logged in
            logoutItem.setVisible(true);
            profileItem.setVisible(true);
            hospitalListItem.setVisible(true);
            questionListItem.setVisible(true);

            loginItem.setVisible(false);
            regItem.setVisible(false);

        } else {                                 // when logged out
            logoutItem.setVisible(false);
            profileItem.setVisible(false);
            hospitalListItem.setVisible(false);
            questionListItem.setVisible(false);

            loginItem.setVisible(true);
            regItem.setVisible(true);

        }
        return menu;
    }


    public MenuItem createMenuItems(MenuItem item, Context context) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.register:
                i = new Intent(context, SignUpActivity.class);
                context.startActivity(i);
                break;
            case R.id.login:
                i = new Intent(context, LoginActivity.class);
                context.startActivity(i);
                break;
            case R.id.profile:
                i = new Intent(context, UserProfileActivity.class);
                context.startActivity(i);
                break;
            case R.id.hospitalList:
                i = new Intent(context, HospitalList.class);
                context.startActivity(i);
                break;
            case R.id.questionList:
                i = new Intent(context, QuestionActivity.class);
                context.startActivity(i);
                break;
            case R.id.logout:
                createLogoutAlert(context);
                break;
            case R.id.countries:
                i = new Intent(context, NationalCovidStats.class);
                context.startActivity(i);
                break;
            default:
                break;
        }
        return item;
    }

    public void logoutWithoutAlert(final Context context) {
        sharedPreferences = context.getSharedPreferences("covidManagement", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("refreshToken");
        editor.commit();
        Intent toMain = new Intent(context, MainActivity.class);
        toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(toMain);
    }


    public void createLogoutAlert(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("You sure?").setCancelable(false)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("token");
                        editor.remove("refreshToken");
                        editor.commit();
                        if (currentMenu != null) {
                            checkMenuItems(currentMenu, context);
                        }
                        Intent toMain = new Intent(context, MainActivity.class);
                        toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(toMain);

                        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Attempting Logout");
        dialog.show();
    }

    public void diplayAlert(final Context context, final String covidResult) {

        Log.i("My context is here", context.toString());
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
        alertdialog.setTitle("Covid Result:" + covidResult);
        alertdialog.setIcon(R.drawable.skip_icon)
                .setMessage("Monitor your symtoms and take precautions")
                .setCancelable(false)
                .setPositiveButton("Take Action", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Proceeding", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, HospitalList.class);
                        context.startActivity(i);
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertdialog.create();

        alertDialog.show();
    }

   public void enableLocation(final Context context)
   {

       locationPreferences = context.getSharedPreferences("covidManagement", MODE_PRIVATE);
       locationEditor = locationPreferences.edit();
       fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
       PermissionListener listener = new PermissionListener() {
           @Override
           public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
               Toast.makeText(context, "Location Permission Granted", Toast.LENGTH_LONG).show();
               fetchLocationData(context);
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
                   Toast.makeText(context, "Location Permission denied", Toast.LENGTH_LONG).show();
               }

           }

           @Override
           public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
               permissionToken.continuePermissionRequest();

           }
       };
       requestForLocation(context,listener);


   }

    private void requestForLocation(Context context,PermissionListener listener) {
        Dexter.withContext(context)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(listener).check();
    }


    private void fetchLocationData(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        else{

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location!=null)
                    {
                        double longi = location.getLongitude();
                        double lati = location.getLatitude();
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

}
