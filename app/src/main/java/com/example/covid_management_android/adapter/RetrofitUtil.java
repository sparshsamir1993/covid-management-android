package com.example.covid_management_android.adapter;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    private Retrofit retrofit;
    private String baseUrl;

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public String getBaseUrl() {
        return baseUrl;
    }



    public RetrofitUtil(String baseUrl){
        try{
            this.baseUrl = baseUrl;
            Log.i("url is ", baseUrl);
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create());
            Log.i("builder is ", builder.toString());
            retrofit = builder.build();

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
