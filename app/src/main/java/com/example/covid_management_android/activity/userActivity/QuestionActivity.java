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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //initializing up retrofit

       // retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/question/");
        retrofitUtil = new RetrofitUtil("http://192.168.0.105:5050/api/v1/user/question/");
        retrofit = retrofitUtil.getRetrofit();
        userClient = retrofit.create(UserClient.class);
        myRecyclerView = findViewById(R.id.questionRecycle);
        sharedPreferences = getSharedPreferences("covidManagement",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        myQuestionResponse = findViewById(R.id.myquestionResponse);
       // Integer covidQuestionFlag = sharedPreferences.getInt("QuestionnaireSubmission",0);

        if(getIntent().getExtras()!=null)
        {
            String result  = getIntent().getExtras().get("filled").toString();
            try{
                filledOptionData = new JSONArray(result);
            }catch(Exception e){
                e.printStackTrace();
            }
            Log.i("HEELOOOOO THERE",result);
        }


        fetchQuestionData();

    }

    private void fetchQuestionData() {
        String token = sharedPreferences.getString("token", null);
        String refreshToken = sharedPreferences.getString("refreshToken", null);

        if(token.split("JWT ").length ==1){
            token = "JWT "+token;
        }

            Call<List<Question>> myQuestion;
            myQuestion = userClient.fetchQuestions(token,refreshToken);
            myQuestion.enqueue(new Callback<List<Question>>() {
                @Override
                public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                    List<Question> questions = response.body();
                    myquestionAdapter = new QuestionAdapter(questions, QuestionActivity.this, sharedPreferences, myQuestionResponse, editor, filledOptionData);
                    mylayoutmanager = new LinearLayoutManager(QuestionActivity.this);
                    myRecyclerView.setLayoutManager(mylayoutmanager);
                    myRecyclerView.setAdapter(myquestionAdapter);
                }
                @Override
                public void onFailure(Call<List<Question>> call, Throwable t) {

                    Toast.makeText(QuestionActivity.this, "Could not load Questions", Toast.LENGTH_LONG).show();
                }
            });


    }




}