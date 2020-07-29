package com.example.covid_management_android.activity.userActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.AppUtil;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.CurrentUser;

import com.example.covid_management_android.model.UserSubmission.UserSubmittedAnswers;
import com.example.covid_management_android.service.UserClient;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CovidQuestionnaireRedirection extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button survey;
    SharedPreferences sharedPreferences;
    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    UserClient userClient;
    JSONArray myUserfilledresponses;
    AppUtil appUtil = new AppUtil();

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String token, refreshToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_questionnaire_redirection);
        survey = findViewById(R.id.btnsurvey);
        sharedPreferences = getSharedPreferences("covidManagement", MODE_PRIVATE);
        myUserfilledresponses = new JSONArray();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        appUtil.checkMenuItems(navigationView.getMenu(), CovidQuestionnaireRedirection.this);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/questionResponse/");
        //retrofitUtil = new RetrofitUtil("http://192.168.0.105:5050/api/v1/user/questionResponse/");
        retrofit = retrofitUtil.getRetrofit();
        retrofitUtil.setContext(CovidQuestionnaireRedirection.this);
        userClient = retrofit.create(UserClient.class);

        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token = sharedPreferences.getString("token", null);
                refreshToken = sharedPreferences.getString("refreshToken", null);
                if (token == null || refreshToken == null) {
                    Toast.makeText(getApplicationContext(), "Token Invalid", Toast.LENGTH_SHORT).show();
                } else {
                    getfilledQuestionnaire();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        appUtil.createMenuItems(menuItem, CovidQuestionnaireRedirection.this);
        return true;
    }

    private void getfilledQuestionnaire() {
        token = sharedPreferences.getString("token", null);
        refreshToken = sharedPreferences.getString("refreshToken", null);
        Integer userId = sharedPreferences.getInt("userId", 1);
        CurrentUser user = new CurrentUser();
        user.setUserId(userId);
        if (token.split("JWT ").length == 1) {
            token = "JWT " + token;
        }
        Intent i = new Intent(CovidQuestionnaireRedirection.this, QuestionActivity.class);
//        Log.i("json array --- ", myUserfilledresponses.toString());
//        i.putExtra("filled", myUserfilledresponses.toString());
        startActivity(i);
//        myUserfilledresponses =  appUtil.getUserQuestionsResponse(userClient,token, refreshToken,user);
//        Log.i("responses", myUserfilledresponses.toString());
//        Call<List<UserSubmittedAnswers>> call = userClient.fetchData(token, refreshToken, user);
//        call.enqueue(new Callback<List<UserSubmittedAnswers>>() {
//            @Override
//            public void onResponse(Call<List<UserSubmittedAnswers>> call, Response<List<UserSubmittedAnswers>> response) {
//                if (response.isSuccessful()) {
//                    List<UserSubmittedAnswers> list = response.body();
//                    try {
//                        for (UserSubmittedAnswers n : list) {
//                            JSONObject optionData = new JSONObject();
//                            optionData.put("optionId", n.getOptionId());
//                            optionData.put("id", n.getId());
//                            myUserfilledresponses.put(optionData);
//                        }
//                        Intent i = new Intent(CovidQuestionnaireRedirection.this, QuestionActivity.class);
//                        Log.i("json array --- ", myUserfilledresponses.toString());
//                        i.putExtra("filled", myUserfilledresponses.toString());
//                        startActivity(i);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    switch (response.code()) {
//                        case 403:
//                        case 401:
//                            Toast.makeText(getApplicationContext(), "Error fetching user response data", Toast.LENGTH_SHORT).show();
//                            break;
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<UserSubmittedAnswers>> call, Throwable t) {
//                Log.i("FAILED HERE ", t.getMessage());
//            }
//        });


    }
}
