package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.AppUtil;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.AuthToken;
import com.example.covid_management_android.model.CurrentUser;
import com.example.covid_management_android.model.Login;
import com.example.covid_management_android.model.User;
import com.example.covid_management_android.model.UserSubmission.UserSubmittedAnswers;
import com.example.covid_management_android.service.UserClient;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    Button callSignup,Login_btn;
    TextView logoText, sloganText;
    ImageView image;
    TextInputLayout username,password;

    EditText emailField, passwordField;
    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    UserClient userClient;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    AppUtil appUtil = new AppUtil();
    List<UserSubmittedAnswers> list;

//**************    menu  **************//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation_menu, menu);
        menu = appUtil.checkMenuItems(menu, LoginActivity.this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item = appUtil.createMenuItems(item, LoginActivity.this);
        return super.onOptionsItemSelected(item);
    }

    public void loginSubmit(View view){
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        Login login = new Login(email, password);

        Call<AuthToken> call = userClient.login(login);
        Log.i("login url is ",call.request().url().toString());
        call.enqueue(new Callback<AuthToken>() {
            @Override
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                if(response.isSuccessful()){
                    String token = response.body().getToken();
                    String refreshToken = response.body().getRefreshToken();
                    Integer id = response.body().getId();
                    Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                    editor.putString("token", token);
                    editor.putString("refreshToken", refreshToken);
                    Log.i("My user Id",id.toString());
                    editor.putInt("userId",id);
                    editor.commit();

                        Intent i = new Intent(getApplicationContext(), CovidQuestionnaireRedirection.class);
                        startActivity(i);

                }else{
                    switch (response.code()){
                        case 403:
                        case 401:
                            Toast.makeText(getApplicationContext(), "UserName/Password do not match", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), "Error Occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams);
        setContentView(R.layout.activity_login);

        //hooks
        image = findViewById(R.id.logo_Image);
        logoText = findViewById(R.id.logo_name);
        //sloganText = findViewById(R.id.slogan_Name);
       // username = findViewById(R.id.logo_user);
       // password = findViewById(R.id.passwordField);
       // Login_btn = findViewById(R.id.loginButton);


        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/");
        retrofit = retrofitUtil.getRetrofit();
        userClient = retrofit.create(UserClient.class);
        callSignup = findViewById(R.id.callSignup);


        sharedPreferences = getSharedPreferences("covidManagement",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        callSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (LoginActivity.this,SignUpActivity.class);

                Pair[] pairs = new Pair[2];

                pairs[0] = new Pair<View,String>(image,"logo_image");
                pairs[1] = new Pair<View,String>(logoText,"logo_text");
               // pairs[2] = new Pair<View,String>(sloganText,"logo_dec");
               // pairs[3] = new Pair<View,String>(username,"logo_user");
               // pairs[4] = new Pair<View,String>(password,"logo_pwd");
               // pairs[3] = new Pair<View,String>(Login_btn,"logo_btn");
                //pairs[4] = new Pair<View,String>(callSignup,"logo_sbtn");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
                startActivity(intent,options.toBundle());
            }
        });


    }



}