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
import com.example.covid_management_android.model.Country;
import com.example.covid_management_android.model.CovidStats;
import com.example.covid_management_android.model.CurrentUser;

import com.example.covid_management_android.model.Login;
import com.example.covid_management_android.model.UserSubmission.UserSubmittedAnswers;
import com.example.covid_management_android.service.UserClient;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.RotatingCircle;
import com.leo.simplearcloader.SimpleArcLoader;

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

    JSONArray covidStats;
    View line1, line2, line3, line4, line5, line6, line7;
    TextView txtCases, txtRecovered, txtCritical, txtDeath, txtTotalTest, txtTodaycases, txtTodayDeath, txtTodayRecover;
    TextView caseCount, recoveryCount, critiCount, deathCount, testCount, todayTestCount, todayDeathCount, todayRecoverCount;
    ScrollView statsScroll;
    AnyChartView covidChart;
    ProgressBar progressBar;
    SimpleArcLoader loader;
    Integer check;
    Country myCountryData;


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
        //progressBar = findViewById(R.id.spin_kit);
        covidChart = findViewById(R.id.covidChart);
        statsScroll = findViewById(R.id.statsScroll);
        covidStats = new JSONArray();

        loader = findViewById(R.id.loader);

        loader.start();

        check = getIntent().getIntExtra("check",0);

        myCountryData = (Country) getIntent().getSerializableExtra("CounrtyData");
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("COVID global stats");
        setSupportActionBar(toolbar);

        Log.i("In here>>",String.valueOf(check));

        if(check == 0) {
            getCovidStats();
        }
        else
        {
            if(myCountryData!=null) {
                printNationalStats(myCountryData);
            }
        }


        sharedPreferences = getSharedPreferences("covidManagement", MODE_PRIVATE);
        myUserfilledresponses = new JSONArray();


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        appUtil.checkMenuItems(navigationView.getMenu(), CovidQuestionnaireRedirection.this);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
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

    private void printNationalStats(Country data) {

        toolbar.setTitle(data.getCountry() + " Covid Statistics");
        caseCount.setText(data.getCases());
        recoveryCount.setText(data.getRecovered());
        critiCount.setText(data.getCritical());
        deathCount.setText(data.getDeaths());
        testCount.setText(data.getTests());
        todayTestCount.setText(data.getTodayCases());
        todayDeathCount.setText(data.getTodayDeaths());
        todayRecoverCount.setText(data.getTodayRecovered());

        List<DataEntry> covidStatsEntries =  addPieChartData(caseCount.getText().toString(),deathCount.getText().toString(),recoveryCount.getText().toString());
        loadCovidStatsPieChart(covidStatsEntries);

    }

    private List<DataEntry> addPieChartData(String caseCount, String deathCount, String recoveryCount) {
        List<DataEntry> myCovidStatsData = new ArrayList<>();
        myCovidStatsData.add(new ValueDataEntry("Cases", Long.parseLong(caseCount)));
        myCovidStatsData.add(new ValueDataEntry("Deaths", Long.parseLong(deathCount)));
        myCovidStatsData.add(new ValueDataEntry("Recovered", Long.parseLong(recoveryCount)));
        return myCovidStatsData;

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
                if (response.isSuccessful()) {
                    String cases = String.valueOf(response.body().getCases());
                    String recovered = String.valueOf(response.body().getRecovered());
                    String critical = String.valueOf(response.body().getCritical());
                    String deaths = String.valueOf(response.body().getDeaths());
                    String totalTests = String.valueOf(response.body().getTests());
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

                    List<DataEntry> myCovidStatsData = addPieChartData(todayCases,todayDeaths,todayRecovered);
                    loadCovidStatsPieChart(myCovidStatsData);


                } else {
                    Toast.makeText(CovidQuestionnaireRedirection.this, "Error fetching updates", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CovidStats> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(CovidQuestionnaireRedirection.this, "error", Toast.LENGTH_LONG).show();
            }
        });
        statsScroll.setVisibility(View.VISIBLE);
        statsScroll.setSmoothScrollingEnabled(true);
    }

    private void loadCovidStatsPieChart(List<DataEntry> mycovidStats) {

        Pie piechart = AnyChart.pie();
        piechart.data(mycovidStats);
        covidChart.setChart(piechart);
        loader.stop();
        loader.setVisibility(View.GONE);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        appUtil.createMenuItems(menuItem, CovidQuestionnaireRedirection.this);
        return true;
    }

    private void getfilledQuestionnaire() {

        String token = sharedPreferences.getString("token", null);
        String refreshToken = sharedPreferences.getString("refreshToken", null);
        Integer userId = sharedPreferences.getInt("userId", 1);
        CurrentUser user = new CurrentUser();
        user.setUserId(userId);
        if (token.split("JWT ").length == 1) {
            token = "JWT " + token;
        }
        Intent i = new Intent(CovidQuestionnaireRedirection.this, QuestionActivity.class);
        startActivity(i);
    }
}
