package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
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
    Button calllogin,btnquestion;
    UserClient userClient;

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
        btnquestion = findViewById(R.id.btnquestion);

        //initializing up retrofit

        retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/");
        retrofit = retrofitUtil.getRetrofit();
        userClient = retrofit.create(UserClient.class);

        btnquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,QuestionActivity.class);
                startActivity(i);
            }
        });

        calllogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (SignUpActivity.this,LoginActivity.class);

                //Pair[] pairs = new Pair[2];

               // pairs[0] = new Pair<View,String>(image,"logo_image");
               // pairs[1] = new Pair<View,String>(logoText,"logo_text");
                // pairs[2] = new Pair<View,String>(sloganText,"logo_dec");
                // pairs[3] = new Pair<View,String>(username,"logo_user");
                // pairs[4] = new Pair<View,String>(password,"logo_pwd");
                // pairs[3] = new Pair<View,String>(Login_btn,"logo_btn");
                //pairs[4] = new Pair<View,String>(callSignup,"logo_sbtn");

               // ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUpActivity.this,pairs);
                startActivity(intent);
                // startActivity(intent,options.toBundle());
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

