package com.example.covid_management_android.activity.userActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.CountryAdapter;
import com.example.covid_management_android.adapter.HospitalAdapter;
import com.example.covid_management_android.model.Country;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.service.UserClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NationalCovidStats extends AppCompatActivity {

    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    UserClient userClient;
    RecyclerView.LayoutManager mylayoutmanager;
    RecyclerView myRecyclerView;
    EditText searchCountry;
    CountryAdapter myCountryAdapter;
    List<Country> data;
    List<Country> filteredList;
    boolean isUserSearched;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_national_covid_stats);

        retrofitUtil = new RetrofitUtil("https://corona.lmao.ninja/v3/covid-19/");
        retrofit = retrofitUtil.getRetrofit();
        retrofitUtil.setContext(NationalCovidStats.this);
        userClient = retrofit.create(UserClient.class);
        myRecyclerView = findViewById(R.id.countryRecycle);
        searchCountry = findViewById(R.id.searchcountry);
        isUserSearched = false;
        toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("");
        toolbar.hideOverflowMenu();
        toolbar.setNavigationIcon(R.drawable.nav_back_button);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchCountriesData();
        searchCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isUserSearched = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

                filterCountries(s.toString());

            }

        });
        
    }

    private void filterCountries(String countryName) {

        countryName = countryName.toLowerCase().trim();
        filteredList = new ArrayList<>();
        for(Country current : data)
        {
            if(current.getCountry().toLowerCase().trim().contains(countryName))
            {
                filteredList.add(current);

            }

        }
        myCountryAdapter.filteredCountryList(filteredList);
    }

    private void fetchCountriesData() {

        Call<List<Country>> mCountryData;
        mCountryData = userClient.fetchCountriesAndStats();
        mCountryData.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if(response.isSuccessful())
                {
                    data = response.body();
                    myCountryAdapter = new CountryAdapter(data,NationalCovidStats.this);
                    mylayoutmanager = new LinearLayoutManager(NationalCovidStats.this);
                    myRecyclerView.setLayoutManager(mylayoutmanager);
                    myRecyclerView.setAdapter(myCountryAdapter);
                    myCountryAdapter.onCountryClick(new CountryAdapter.OnCountryCardListener() {
                        @Override
                        public void onCardClick(int position) {
                            Country countryData;
                            if(isUserSearched) {
                                 countryData = filteredList.get(position);
                            }
                            else
                            {
                                countryData = data.get(position);
                            }
                            Log.i("Country here",countryData.getCountry());
                            Intent i = new Intent(NationalCovidStats.this,CovidQuestionnaireRedirection.class);
                            i.putExtra("CounrtyData",countryData);
                            i.putExtra("check",1);
                            startActivity(i);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {

            }
        });

    }

}