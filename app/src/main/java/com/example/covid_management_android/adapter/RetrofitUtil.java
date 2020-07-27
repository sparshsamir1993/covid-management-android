package com.example.covid_management_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_management_android.activity.userActivity.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil extends AppCompatActivity {
    private Retrofit retrofit;
    private String baseUrl;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public String getBaseUrl() {
        return baseUrl;
    }


    AppUtil appUtil = new AppUtil();
    public RetrofitUtil(String baseUrl){
        //try{
            this.baseUrl = baseUrl;
            Log.i("url is ", baseUrl);
            OkHttpClient.Builder client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @NotNull
                @Override
                public Response intercept(@NotNull Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    if(response.isSuccessful()){
                        try{
                            String token = response.header("token", null);
                            String refreshToken = response.header("refresh-token", null);
                            if(token != null && refreshToken != null){
                                SharedPreferences sharedPreferences = context.getSharedPreferences("covidManagement",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", token);
                                editor.putString("refreshToken", refreshToken);
                                editor.commit();
                                Log.i("response is -- ",response.header("token"));
                            }

                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        Intent i;

                        switch(response.code()){
                            case 500:
                                Log.i("In the intercept ", "Entered");
                                appUtil.logoutWithoutAlert(context);
                                break;
                            default:
                                return response;

                        }
                    }
                    return response;
                }
            });
            client.connectTimeout(30, TimeUnit.SECONDS);
            client.readTimeout(30, TimeUnit.SECONDS);
            client.writeTimeout(30, TimeUnit.SECONDS);
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(baseUrl).client(client.build()).addConverterFactory(GsonConverterFactory.create());

            retrofit = builder.build();


    }
}
