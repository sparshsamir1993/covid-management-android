package com.example.covid_management_android.activity.userActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.covid_management_android.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPALSH_SCREEN = 5000;

    //variables
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo, slogan;

    SharedPreferences onBoardingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //hooks
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView4);
        slogan = findViewById(R.id.textView5);


        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {

            public void run() {

                onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = onBoardingScreen.getBoolean("firstTime", true);

                if (isFirstTime) {

                    Intent toBoarding = new Intent(getApplicationContext(), OnBoarding.class);
                    startActivity(toBoarding);


                } else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            }

            // Intent intent = new Intent(SplashScreen.this,OnBoarding.class);

            // Pair[] pairs = new Pair[2];
            // pairs[0] = new Pair<View,String>(image,"logo_image");
            // pairs[1] = new Pair<View,String>(logo,"logo_text");

            // ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this,pairs);
            //startActivity(intent,options.toBundle());
            // void startActivity(intent);

        }, SPALSH_SCREEN);
    }
}


//   },SPALSH_SCREEN);
// public void run() {}



