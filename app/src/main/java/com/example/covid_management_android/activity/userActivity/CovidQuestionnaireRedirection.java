package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.AppUtil;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.CovidStats;
import com.example.covid_management_android.model.CurrentUser;

import com.example.covid_management_android.model.Login;
import com.example.covid_management_android.model.UserSubmission.UserSubmittedAnswers;
import com.example.covid_management_android.service.UserClient;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.RotatingCircle;

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
    JSONArray covidStats;
    View line1,line2,line3,line4,line5,line6,line7;
    TextView txtCases,txtRecovered,txtCritical,txtDeath,txtTotalTest,txtTodaycases,txtTodayDeath,txtTodayRecover;
    TextView caseCount,recoveryCount,critiCount,deathCount,testCount,todayTestCount,todayDeathCount,todayRecoverCount;
    ScrollView statsScroll;
    AnyChartView covidChart;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_questionnaire_redirection);

        survey = findViewById(R.id.btnsurvey);
        txtCases = findViewById(R.id.txtcases);
        txtRecovered = findViewById(R.id.txtrecovered);
        txtCritical = findViewById(R.id.txtcritical);
        txtDeath = findViewById(R.id.txtdeaths);
        txtTotalTest = findViewById(R.id.txttest);
        txtTodaycases = findViewById(R.id.txttodaycases);
        txtTodayDeath = findViewById(R.id.txttodaydeaths);
        txtTodayRecover = findViewById(R.id.txttodayrecover);

        caseCount = findViewById(R.id.casescount);
        recoveryCount = findViewById(R.id.recovercount);
        critiCount = findViewById(R.id.criticalcount);
        deathCount = findViewById(R.id.deathcount);
        testCount = findViewById(R.id.testcount);
        todayTestCount = findViewById(R.id.todaycasescount);
        todayDeathCount = findViewById(R.id.todaydeathcount);
        todayRecoverCount = findViewById(R.id.todayrecovercount);

        line1 = findViewById(R.id.line1);
        line2 = findViewById(R.id.line2);
        line3 = findViewById(R.id.line3);
        line4 = findViewById(R.id.line4);
        line5 = findViewById(R.id.line5);
        line6 = findViewById(R.id.line6);
        line7 = findViewById(R.id.line7);
        progressBar = findViewById(R.id.spin_kit);
        covidChart = findViewById(R.id.covidChart);
        statsScroll = findViewById(R.id.statsScroll);
        covidStats =  new JSONArray();

        getCovidStats();

        Sprite bounce = new RotatingCircle();
        progressBar.setIndeterminateDrawableTiled(bounce);
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

    private void getCovidStats() {

        retrofitUtil = new RetrofitUtil("https://corona.lmao.ninja/v3/covid-19/");
        retrofit = retrofitUtil.getRetrofit();
        retrofitUtil.setContext(CovidQuestionnaireRedirection.this);
        userClient = retrofit.create(UserClient.class);

        Call<CovidStats> call = userClient.fetchCovidStats();
        call.enqueue(new Callback<CovidStats>() {
            @Override
            public void onResponse(Call<CovidStats> call, Response<CovidStats> response) {
                if(response.isSuccessful())
                {
                    String cases = String.valueOf(response.body().getCases());
                    String recovered = String.valueOf(response.body().getRecovered());
                    String critical = String.valueOf(response.body().getCritical());
                    String deaths = String.valueOf(response.body().getDeaths());
                    String totalTests = String.valueOf(response.body().getTodayDeaths());
                    String todayCases = String.valueOf(response.body().getTodayCases());
                    String todayDeaths = String.valueOf(response.body().getTodayDeaths());
                    String todayRecovered = String.valueOf(response.body().getTodayRecovered());

                    caseCount.setText(cases);
                    recoveryCount.setText(recovered);
                    critiCount.setText(critical);
                    deathCount.setText(deaths);
                    testCount.setText(totalTests);
                    todayTestCount.setText(todayCases);
                    todayDeathCount.setText(todayDeaths);
                    todayRecoverCount.setText(todayRecovered);

                    List<DataEntry> myCovidStatsData = new ArrayList<>();
                    myCovidStatsData.add(new ValueDataEntry("Cases",Long.parseLong(todayCases)));
                    myCovidStatsData.add(new ValueDataEntry("Deaths",Long.parseLong(todayDeaths)));
                    myCovidStatsData.add(new ValueDataEntry("Recovered",Long.parseLong(todayRecovered)));

                    loadCovidStatsPieChart(myCovidStatsData);


                }
                else
                {
                    Toast.makeText(CovidQuestionnaireRedirection.this,"Error fetching updates",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CovidStats> call, Throwable t) {
            Toast.makeText(CovidQuestionnaireRedirection.this,"error",Toast.LENGTH_LONG).show();
            }
        });
        statsScroll.setVisibility(View.VISIBLE);
    }

    private void loadCovidStatsPieChart(List<DataEntry> mycovidStats) {
        Pie piechart = AnyChart.pie();
        piechart.data(mycovidStats);
        covidChart.setChart(piechart);
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
        //retrofitUtil = new RetrofitUtil("http://192.168.0.105:5050/api/v1/user/questionResponse/");
        retrofit = retrofitUtil.getRetrofit();
        retrofitUtil.setContext(CovidQuestionnaireRedirection.this);
        userClient = retrofit.create(UserClient.class);

        Call<List<UserSubmittedAnswers>> call = userClient.fetchData(token,refreshToken,user);
        call.enqueue(new Callback<List<UserSubmittedAnswers>>() {
            @Override
            public void onResponse(Call<List<UserSubmittedAnswers>> call, Response<List<UserSubmittedAnswers>> response) {
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
