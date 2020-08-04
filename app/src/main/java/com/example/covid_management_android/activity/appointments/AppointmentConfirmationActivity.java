package com.example.covid_management_android.activity.appointments;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.covid_management_android.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.covid_management_android.constants.Constants.HOSPITAL_DATA;
import static com.example.covid_management_android.constants.Constants.SHARED_PREF_MAIN_NAME;
import static com.example.covid_management_android.constants.Constants.USER_EMAIL;

public class AppointmentConfirmationActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView emailVal, slotVal, hospitalValue;
    JSONObject currHospitalData;
    Button confirmBookingBtn;
    Date selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_confirmation);

        emailVal = findViewById(R.id.emailValue);
        slotVal = findViewById(R.id.slotValue);
        hospitalValue = findViewById(R.id.hospitalValue);

        Long selectedSlot = getIntent().getLongExtra("selectedSlot", 0);
        Long date = getIntent().getLongExtra("selectedDate", -1);
        selectedDate = new Date(date);
        final String dateString = new SimpleDateFormat("yyyy-MMMM-dd").format(selectedDate);
        Log.i("hhhh -- ",date.toString());
        sharedPreferences = getSharedPreferences(SHARED_PREF_MAIN_NAME,MODE_PRIVATE);
        String email = sharedPreferences.getString(USER_EMAIL,null);
        String hospDataString = getIntent().getStringExtra(HOSPITAL_DATA);
        Long dateLong = getIntent().getLongExtra("selectedDate", -1);
        confirmBookingBtn = findViewById(R.id.confirmBookingBtn);

        confirmBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentConfirmationActivity.this);
                builder.setTitle("About to confirm Appointment");
                builder.setMessage("Appointment to be set at \n"+slotVal.getText());
                builder.setPositiveButton("Book !", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        try{
            currHospitalData = new JSONObject(hospDataString);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(email!=null){
            Log.i("email in sp -- ", email +" "+ selectedSlot);
            emailVal.setText(email);
            String slotText ="";
            if(selectedSlot < 12){
                slotText = selectedSlot+":00 am, "+dateString;
            }else if(selectedSlot == 12){
                slotText = selectedSlot+":00 pm, "+dateString;
            }else{
                slotText = (selectedSlot-12)+":00 pm, "+dateString;
            }
            slotVal.setText(slotText);
            try{

                hospitalValue.setText(currHospitalData.getString("hospitalAddress"));

            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }
}