package com.system.studentmanagement.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.system.studentmanagement.R;
import com.system.studentmanagement.util.Constants;

/*
 * @author Sahil Puri
 * SplashScreen Activity
 * The first screen which shows the logo and name of the app
 */
public class SplashScreenActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreenActivity.this.startActivity(new Intent(SplashScreenActivity.this, ShowStudentsActivity.class));
                finish();
            }
        }, Constants.SPLASH_SCREEN_TIME);


    }
}
