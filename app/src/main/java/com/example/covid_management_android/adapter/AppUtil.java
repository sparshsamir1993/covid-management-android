package com.example.covid_management_android.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_management_android.R;
import com.example.covid_management_android.activity.userActivity.LoginActivity;
import com.example.covid_management_android.activity.userActivity.MainActivity;
import com.example.covid_management_android.activity.userActivity.SignUpActivity;
import com.example.covid_management_android.activity.userActivity.UserProfileActivity;

public class AppUtil extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Menu currentMenu;




    public Menu checkMenuItems(Menu menu, Context context){
        sharedPreferences = context.getSharedPreferences("covidManagement", MODE_PRIVATE);
        currentMenu = menu;

        MenuItem loginItem = menu.findItem(R.id.login);
        MenuItem regItem = menu.findItem(R.id.register);
        MenuItem logoutItem = menu.findItem(R.id.logout);
        MenuItem profileItem = menu.findItem(R.id.profile);

        String token = sharedPreferences.getString("token", null);
        if(token != null){                      // when logged in
            loginItem.setVisible(false);
            regItem.setVisible(false);
            logoutItem.setVisible(true);
            profileItem.setVisible(true);
        }else{                                 // when logged out
            logoutItem.setVisible(false);
            profileItem.setVisible(false);
            loginItem.setVisible(true);
            regItem.setVisible(true);

        }
        return menu;
    }


    public MenuItem createMenuItems(MenuItem item, Context context){
        Intent i;
        switch (item.getItemId()){
            case R.id.register:
                i = new Intent(context, SignUpActivity.class);
                context.startActivity(i);
                break;
            case R.id.login:
                i  = new Intent(context, LoginActivity.class);
                context.startActivity(i);
                break;
            case R.id.profile:
                i  = new Intent(context, UserProfileActivity.class);
                context.startActivity(i);
                break;
            case R.id.logout:
                createLogoutAlert(context);
                break;
            default:
                break;
        }
        return item;
    }

    public void logoutWithoutAlert(final Context context){
        sharedPreferences = context.getSharedPreferences("covidManagement", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("refreshToken");
        editor.commit();
        Intent toMain = new Intent(context, MainActivity.class);
        context.startActivity(toMain);
    }



    public void createLogoutAlert(final Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("You sure?").setCancelable(false)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.remove("token");
                        editor.remove("refreshToken");
                        editor.commit();
                        if(currentMenu != null){
                            checkMenuItems(currentMenu, context);
                        }
                        Intent toMain = new Intent(context, MainActivity.class);
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

    public void diplayAlert(final Context context, final String covidResult)
    {


            AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);
            alertdialog.setTitle("Covid Result");
            alertdialog.setIcon(R.drawable.skip_icon)
                    .setMessage("Your Symptoms are matched")
                    .setCancelable(false)
                    .setPositiveButton("Take Action", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(context, "Procedding", Toast.LENGTH_LONG).show();
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
}
