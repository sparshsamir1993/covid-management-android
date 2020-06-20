package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class SignUpActivity extends AppCompatActivity {

    EditText emailField, passwordField, confirmPasswordField;
    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    UserClient userClient;



    public void signupSubmit(View view){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();
        if(!password.equals(confirmPassword)){
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }
        Login login = new Login(email, password);
        Call<User> call = userClient.signup(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try{
                    int id = response.body().getId();
                    if(id > 0){
                        Toast.makeText(getApplicationContext(), "Sign up complete", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);

        //initializing up retrofit

        retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/");
        retrofit = retrofitUtil.getRetrofit();
        userClient = retrofit.create(UserClient.class);




    }
}