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
import com.example.covid_management_android.activity.LoginActivity;
import com.example.covid_management_android.activity.SignUpActivity;

public class AppUtil extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Menu currentMenu;

    public Menu checkMenuItems(Menu menu, Context context){
        sharedPreferences = context.getSharedPreferences("covidManagement", MODE_PRIVATE);
        currentMenu = menu;
        MenuItem loginItem = menu.findItem(R.id.login);
        MenuItem regItem = menu.findItem(R.id.register);
        MenuItem logoutItem = menu.findItem(R.id.logout);
        String token = sharedPreferences.getString("token", null);
        Log.i("token is ---", token+"");
        if(token != null){
            loginItem.setVisible(false);
            regItem.setVisible(false);
            logoutItem.setVisible(true);
        }else{
            logoutItem.setVisible(false);
            loginItem.setVisible(true);
            regItem.setVisible(true);
        }
        return menu;
    }


    public MenuItem createMenuItems(MenuItem item, Context context){
        Intent i;
        switch (item.getItemId()){
            case R.id.login:
                i  = new Intent(context, LoginActivity.class);
                context.startActivity(i);
                break;
            case R.id.profile:
                i  = new Intent(context, SignUpActivity.class);
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
}
