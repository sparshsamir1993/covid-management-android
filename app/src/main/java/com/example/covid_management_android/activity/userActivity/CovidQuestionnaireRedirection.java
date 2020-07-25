package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.AppUtil;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.CurrentUser;

import com.example.covid_management_android.model.UserSubmission.UserSubmittedAnswers;
import com.example.covid_management_android.service.UserClient;
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

public class CovidQuestionnaireRedirection extends AppCompatActivity {

    Button survey;
    SharedPreferences sharedPreferences;
    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    UserClient userClient;
    JSONArray myUserfilledresponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_questionnaire_redirection);

        survey = findViewById(R.id.btnsurvey);
        sharedPreferences = getSharedPreferences("covidManagement", MODE_PRIVATE);
        myUserfilledresponses = new JSONArray();



        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String token = sharedPreferences.getString("token", null);
                String refreshToken = sharedPreferences.getString("refreshToken", null);
                if (token == null || refreshToken == null) {
                    Toast.makeText(getApplicationContext(), "Token Invalid", Toast.LENGTH_SHORT).show();
                } else {

                    getfilledQuestionnaire();
                }
            }
        });
    }

    private void getfilledQuestionnaire() {

        String token = sharedPreferences.getString("token", null);
        String refreshToken = sharedPreferences.getString("refreshToken", null);

        Integer userId = sharedPreferences.getInt("userId", 1);
        CurrentUser user = new CurrentUser();
        user.setUserId(userId);

        if(token.split("JWT ").length ==1){
            token = "JWT "+token;
        }

        retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/questionResponse/");
        retrofit = retrofitUtil.getRetrofit();
        retrofitUtil.setContext(CovidQuestionnaireRedirection.this);
        userClient = retrofit.create(UserClient.class);

        Call<List<UserSubmittedAnswers>> call = userClient.fetchData(token,refreshToken,user);
        call.enqueue(new Callback<List<UserSubmittedAnswers>>() {
            @Override
            public void onResponse(Call<List<UserSubmittedAnswers>> call, Response<List<UserSubmittedAnswers>> response) {
                // Log.i("My User Response",response.body().get(0).getOption().getOptionContent());
                if (response.isSuccessful()) {
                    List<UserSubmittedAnswers> list = response.body();

                    try {
                        for (UserSubmittedAnswers n : list) {
                            JSONObject optionData = new JSONObject();
                            optionData.put("optionId", n.getOptionId());
                            optionData.put("id", n.getId());
                            myUserfilledresponses.put(optionData);
                        }
                        Intent i = new Intent(CovidQuestionnaireRedirection.this, QuestionActivity.class);
                        Log.i("json array --- ", myUserfilledresponses.toString());
                        i.putExtra("filled", myUserfilledresponses.toString());
                        startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    switch (response.code()) {
                        case 403:
                        case 401:
                            Toast.makeText(getApplicationContext(), "Error fetching user response data", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(Call<List<UserSubmittedAnswers>> call, Throwable t) {

                Log.i("FAILED HERE ", t.getMessage());
            }
        });


    }

}
