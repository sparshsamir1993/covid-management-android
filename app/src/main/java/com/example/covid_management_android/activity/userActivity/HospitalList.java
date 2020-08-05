package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.activity.appointments.AppointmentBookingActivity;
import com.example.covid_management_android.adapter.HospitalAdapter;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.HospitalData;
import com.example.covid_management_android.model.Question;
import com.example.covid_management_android.service.UserClient;

import org.json.JSONObject;

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
        // retrofitUtil = new RetrofitUtil("http://192.168.0.105:5050/api/v1/user/hospital/");
        retrofit = retrofitUtil.getRetrofit();
        retrofitUtil.setContext(HospitalList.this);
        userClient = retrofit.create(UserClient.class);
        myRecyclerView = findViewById(R.id.hospitalRecycle);
        sharedPreferences = getSharedPreferences("covidManagement", MODE_PRIVATE);
        getHospitalData();
    }

    private void getHospitalData() {
        float longi = sharedPreferences.getFloat("Longitude", 0);
        float lati = sharedPreferences.getFloat("Latitude", 0);
        String token = sharedPreferences.getString("token", null);
        String refreshToken = sharedPreferences.getString("refreshToken", null);
        if (token.split("JWT ").length == 1) {
            token = "JWT " + token;
        }
        Call<List<HospitalData>> myHospitalData;
        myHospitalData = userClient.fetchHospitals(token, refreshToken, lati, longi);
        myHospitalData.enqueue(new Callback<List<HospitalData>>() {
            @Override
            public void onResponse(Call<List<HospitalData>> call, Response<List<HospitalData>> response) {
                if (response.isSuccessful()) {
                    final List<HospitalData> hospitals = response.body();
//                Log.i("hospital data ", hospitals.get(0).getName());

                    if (hospitals.size() > 0) {

                        HospitalAdapter myHospitalAdapter = new HospitalAdapter(hospitals);
                        mylayoutmanager = new LinearLayoutManager(HospitalList.this);
                        myRecyclerView.setLayoutManager(mylayoutmanager);
                        myRecyclerView.setAdapter(myHospitalAdapter);
                        myHospitalAdapter.onHospitalClick(new HospitalAdapter.OnHospitalCardListener() {
                            @Override
                            public void oncardClick(int position) {
                                Toast.makeText(HospitalList.this, String.valueOf(hospitals.get(position).getId()), Toast.LENGTH_LONG).show();
                                Intent toAppointmentBooking = new Intent(HospitalList.this, AppointmentBookingActivity.class);
                                try{
                                    JSONObject hospData = new JSONObject();
                                    hospData.put("hospitalId", hospitals.get(position).getId());
                                    hospData.put("hospitalAddress", hospitals.get(position).getDetailedAddress());
                                    hospData.put("hospitalName", hospitals.get(position).getName());
                                    toAppointmentBooking.putExtra("hospitalData", hospData.toString());
                                    startActivity(toAppointmentBooking);

                                }catch(Exception e){
                                    e.printStackTrace();
                                }


                            }
                        });
                    } else {
                        Toast.makeText(HospitalList.this, "Error", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<HospitalData>> call, Throwable t) {
                Log.i("feteching failed", t.getMessage());
            }
        });


    }
}