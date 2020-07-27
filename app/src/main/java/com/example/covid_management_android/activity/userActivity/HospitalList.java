package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.HospitalData;
import com.example.covid_management_android.model.Question;
import com.example.covid_management_android.service.UserClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HospitalList extends AppCompatActivity {
    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    UserClient userClient;
    SharedPreferences sharedPreferences;
    RecyclerView.LayoutManager mylayoutmanager;
    RecyclerView myRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);

        retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/hospital/");
        retrofit = retrofitUtil.getRetrofit();
        userClient = retrofit.create(UserClient.class);
        myRecyclerView = findViewById(R.id.hospitalRecycle);
        sharedPreferences = getSharedPreferences("covidManagement",MODE_PRIVATE);

        getHospitalData();

    }

    private void getHospitalData() {
        float longi = sharedPreferences.getFloat("Longitude",0);
        float lati = sharedPreferences.getFloat("Latitude",0);

        Call<List<HospitalData>> myHospitalData;
        myHospitalData = userClient.fetchHospitals(lati,longi);
        myHospitalData.enqueue(new Callback<List<HospitalData>>() {
            @Override
            public void onResponse(Call<List<HospitalData>> call, Response<List<HospitalData>> response) {
                List<HospitalData> hospitals  = response.body();

            }

            @Override
            public void onFailure(Call<List<HospitalData>> call, Throwable t) {
                Log.i("feteching failed",t.getMessage());
            }
        });



    }
}