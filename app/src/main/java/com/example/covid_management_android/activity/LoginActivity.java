package com.example.covid_management_android.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.Login;
import com.example.covid_management_android.model.User;
import com.example.covid_management_android.service.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    EditText emailField, passwordField;
    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    UserClient userClient;


    public void loginSubmit(View view){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        Login login = new Login(email, password);

        Call<User> call = userClient.login(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String email = response.body().getEmail();
                Toast.makeText(LoginActivity.this, "yayyy", Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this, "noppeee", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/");
        retrofit = retrofitUtil.getRetrofit();
        userClient = retrofit.create(UserClient.class);


    }
}