package com.example.covid_management_android.activity.userActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covid_management_android.R;
import com.example.covid_management_android.adapter.AppUtil;
import com.example.covid_management_android.adapter.RetrofitUtil;
import com.example.covid_management_android.model.User;
import com.example.covid_management_android.service.UserClient;

import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserProfileActivity extends AppCompatActivity{

    AppUtil appUtil = new AppUtil();
    EditText nameField, dobField;
    CalendarView dobPicker;
    Calendar calendar = Calendar.getInstance();
    UserClient userClient;
    RetrofitUtil retrofitUtil;
    Retrofit retrofit;
    SharedPreferences sharedPreferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation_menu, menu);
        menu = appUtil.checkMenuItems(menu, UserProfileActivity.this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item = appUtil.createMenuItems( item, UserProfileActivity.this);
        return super.onOptionsItemSelected(item);
    }

    public void onUserUpdate(View view){
        String name = nameField.getText().toString();
        String token = sharedPreferences.getString("token", null);
        String refreshToken = sharedPreferences.getString("refreshToken", null);
        if(token == null || refreshToken == null){
            Toast.makeText(getApplicationContext(), "Token Invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User();
        user.setName(name);
        if(token.split("JWT ").length ==1){
            token = "JWT "+token;
        }

        Call<User> call = userClient.update(token, refreshToken, user);
        Log.i("url is ",call.request().url().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Updated Successfully "+response.code(), Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getApplicationContext(), "Some error occured "+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    public void openDobDialog(){
        try{
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    calendar.set(Calendar.YEAR, i);
                    calendar.set(Calendar.MONTH, i1);
                    calendar.set(Calendar.DAY_OF_MONTH, i2);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    Date dobTime = calendar.getTime();
                    Log.i("dob is --- ", dobTime.toString());
                    dobField.setText(i1+1+"/"+i2+"/"+i);
                }
            };
            new DatePickerDialog(this, date, 1970, 1,1).show();
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        nameField = findViewById(R.id.nameField);
        dobField = findViewById(R.id.dobField);
        dobField.setInputType(InputType.TYPE_NULL);
        dobField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                openDobDialog();

            }
        });


        retrofitUtil = new RetrofitUtil("http://10.0.2.2:5050/api/v1/user/signOn/");
        retrofit = retrofitUtil.getRetrofit();
        retrofitUtil.setContext(UserProfileActivity.this);
        userClient = retrofit.create(UserClient.class);
        sharedPreferences = getSharedPreferences("covidManagement",MODE_PRIVATE);



    }



}