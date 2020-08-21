package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.constants.Constants;
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
    TextView logoText, sloganText;
    ImageView image;
    Retrofit retrofit;
    Button calllogin;
    UserClient userClient;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public void signupSubmit(View view){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();
        if (!validateEmail() | !validationPassword()){
            return;
        }
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
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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


        //hooks
        image = findViewById(R.id.logo_Image);
        logoText = findViewById(R.id.logo_name);

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        confirmPasswordField = findViewById(R.id.confirmPasswordField);
        calllogin = findViewById(R.id.callLogin);

        sharedPreferences = getSharedPreferences("covidManagement",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //initializing up retrofit

        retrofitUtil = new RetrofitUtil(Constants.BASE_URL +"/");
        retrofit = retrofitUtil.getRetrofit();
        userClient = retrofit.create(UserClient.class);

        calllogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (SignUpActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean validateEmail(){

        String value = emailField.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        //String noWhitespace = "(?=\\s+$)";
        if (value.isEmpty()){
            emailField.setError("Email cannot be empty");
            return false;
        }
        else if (!value.matches(emailPattern)){
            emailField.setError("Invalid Email address");
            return false;

        }
        else {
            emailField.setError(null);
            //emailField.setErrorEnabled(false);
            return true;
        }



    }

    private boolean validationPassword(){
        String val = passwordField.getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //at least 4 characters
                "$";

        if (val.isEmpty()) {
            passwordField.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            passwordField.setError("Password is too weak");
            return false;
        } else {
            passwordField.setError(null);
            //passwordField.setErrorEnabled(false);
            return true;
        }
        }





    }

