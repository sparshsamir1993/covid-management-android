package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.QuestionAdapter;
import com.example.covid_management_android.adapter.RetrofitUtil;

import com.example.covid_management_android.model.CurrentUser;
import com.example.covid_management_android.model.Question;
import com.example.covid_management_android.model.UserSubmission.UserSubmittedAnswers;
import com.example.covid_management_android.service.UserClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuestionActivity extends AppCompatActivity {

    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    UserClient userClient;
    QuestionAdapter myquestionAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView.LayoutManager mylayoutmanager;
    RecyclerView myRecyclerView;
    Button myQuestionResponse;
    List<UserSubmittedAnswers> list;
    JSONArray filledOptionData;
    String token, refreshToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //initializing up retrofit

        myRecyclerView = findViewById(R.id.questionRecycle);
        sharedPreferences = getSharedPreferences("covidManagement", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        myQuestionResponse = findViewById(R.id.myquestionResponse);
        // Integer covidQuestionFlag = sharedPreferences.getInt("QuestionnaireSubmission",0);
//
//        if (getIntent().getExtras() != null) {
//            String result = getIntent().getExtras().get("filled").toString();
//            try {
//                filledOptionData = new JSONArray(result);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Log.i("HEELOOOOO THERE", result);
//        }else{
//
//        }

//        fetchUserResponseData();
        fetchQuestionData();

    }

    private void fetchUserResponseData() {
        token = sharedPreferences.getString("token", null);
        refreshToken = sharedPreferences.getString("refreshToken", null);
        final JSONArray result = new JSONArray();
        if (token.split("JWT ").length == 1) {
            token = "JWT " + token;
        }


    }

    private void fetchQuestionData() {
        token = sharedPreferences.getString("token", null);
        refreshToken = sharedPreferences.getString("refreshToken", null);

        if (token.split("JWT ").length == 1) {
            token = "JWT " + token;
        }
        retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/question/");
        // retrofitUtil = new RetrofitUtil("http://192.168.0.105:5050/api/v1/user/question/");
        retrofit = retrofitUtil.getRetrofit();
        retrofitUtil.setContext(QuestionActivity.this);
        userClient = retrofit.create(UserClient.class);

        Integer userId = sharedPreferences.getInt("userId", 1);
        final CurrentUser user = new CurrentUser();
        user.setUserId(userId);

        Call<List<Question>> myQuestion;
        myQuestion = userClient.fetchQuestions(token, refreshToken);
        myQuestion.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                final List<Question> questions = response.body();
                if (response.isSuccessful()) {

                    Log.i("json array --- ", questions.toString());
                    retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/questionResponse/");
                    // retrofitUtil = new RetrofitUtil("http://192.168.0.105:5050/api/v1/user/question/");
                    retrofit = retrofitUtil.getRetrofit();
                    retrofitUtil.setContext(QuestionActivity.this);
                    userClient = retrofit.create(UserClient.class);
                    token = sharedPreferences.getString("token", null);
                    refreshToken = sharedPreferences.getString("refreshToken", null);
//                    if(token)
                    Call<List<UserSubmittedAnswers>> callFilledData = userClient.fetchData(token, refreshToken, user.getUserId());
                    callFilledData.enqueue(new Callback<List<UserSubmittedAnswers>>() {
                        @Override
                        public void onResponse(Call<List<UserSubmittedAnswers>> call2, Response<List<UserSubmittedAnswers>> response2) {
                            Log.i("json array11111111 --- ", response2.code() + "");
                            filledOptionData = new JSONArray();
                            if (response2.isSuccessful()) {
                                List<UserSubmittedAnswers> list = response2.body();
                                try {
                                    for (UserSubmittedAnswers n : list) {
                                        JSONObject optionData = new JSONObject();
                                        optionData.put("optionId", n.getOptionId());
                                        optionData.put("id", n.getId());
//                                    result.put(optionData);
                                        filledOptionData.put(optionData);


                                    }
                                    myquestionAdapter = new QuestionAdapter(questions, QuestionActivity.this, sharedPreferences, myQuestionResponse, editor, filledOptionData);
                                    mylayoutmanager = new LinearLayoutManager(QuestionActivity.this);
                                    myRecyclerView.setLayoutManager(mylayoutmanager);
                                    myRecyclerView.setAdapter(myquestionAdapter);

//                                filledOptionData = result;
//                        Intent i = new Intent(CovidQuestionnaireRedirection.this, QuestionActivity.class);
                                    Log.i("json array --- ", filledOptionData.toString());
//                        i.putExtra("filled", myUserfilledresponses.toString());
//                        startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                switch (response2.code()) {
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

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {

                Toast.makeText(QuestionActivity.this, "Could not load Questions", Toast.LENGTH_LONG).show();
            }
        });


    }


}