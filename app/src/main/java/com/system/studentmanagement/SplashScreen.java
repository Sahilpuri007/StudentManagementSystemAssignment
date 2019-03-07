package com.system.studentmanagement;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.system.studentmanagement.activities.ShowStudentsActivity;

public class SplashScreen extends AppCompatActivity {

    class Splash implements  Runnable{
        Splash(){

        }

        @Override
        public void run() {
            SplashScreen.this.startActivity(new Intent(SplashScreen.this, ShowStudentsActivity.class));
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Splash(), (long) 1000);

    }
}
