package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.QuestionAdapter;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.Question;
import com.example.covid_management_android.model.QuestionOption;
import com.example.covid_management_android.service.UserClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuestionActivity extends AppCompatActivity {

    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    UserClient userClient;
    Call<List<QuestionOption>>  myQuestion;
    RecyclerView.LayoutManager mylayoutmanager;
    RecyclerView myRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //initializing up retrofit

        retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/admin/");
        retrofit = retrofitUtil.getRetrofit();
        userClient = retrofit.create(UserClient.class);
        myRecyclerView = findViewById(R.id.questionRecycle);
        mylayoutmanager = new LinearLayoutManager(this);

        fetchQuestionData();
    }
    private void fetchQuestionData() {
        myQuestion = userClient.fetchQuestions();
        myQuestion.enqueue(new Callback<List<QuestionOption>>() {
            @Override
            public void onResponse(Call<List<QuestionOption>> call, Response<List<QuestionOption>> response) {
                //Log.d("questions arrived",response.body().toArray().toString());
                if(response.isSuccessful()) {
                    List<QuestionOption> myquestions = response.body();
                    QuestionAdapter myadapter = new QuestionAdapter(myquestions);
                    myRecyclerView.setLayoutManager(mylayoutmanager);
                    myRecyclerView.setAdapter(myadapter);
                }
                else
                {
                    Toast.makeText(QuestionActivity.this, "Error while loading...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuestionOption>> call, Throwable t) {
                Log.d("ERROR here",t.getMessage());
                Toast.makeText(QuestionActivity.this, "Failed to load...", Toast.LENGTH_SHORT).show();
            }
        });

    }

}