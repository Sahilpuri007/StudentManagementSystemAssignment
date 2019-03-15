package com.system.studentmanagement;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.system.studentmanagement.activities.ShowStudentsActivity;

/*
 * @author Sahil Puri
 * SplashScreen Activity
 * The first screen which shows the logo and name of the app
 */
public class SplashScreen extends AppCompatActivity {

   private static final long SPLASH_SCREEN_TIME = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, ShowStudentsActivity.class));
                finish();
            }
        }, SPLASH_SCREEN_TIME);


    }
}
