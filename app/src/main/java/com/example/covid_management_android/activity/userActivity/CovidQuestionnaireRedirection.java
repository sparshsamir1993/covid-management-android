package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.model.Question;

public class CovidQuestionnaireRedirection extends AppCompatActivity {

      Button survey;
      SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_questionnaire_redirection);

        survey =  findViewById(R.id.btnsurvey);
        sharedPreferences = getSharedPreferences("covidManagement",MODE_PRIVATE);


        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = sharedPreferences.getString("token", null);
                String refreshToken = sharedPreferences.getString("refreshToken", null);
                if(token == null || refreshToken == null){
                    Toast.makeText(getApplicationContext(), "Token Invalid", Toast.LENGTH_SHORT).show();
                }

                else{
                    Intent i = new Intent(CovidQuestionnaireRedirection.this, QuestionActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}