package com.example.ariel.boodal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ariel.boodal.helper.ConnectionCheck;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ConnectionCheck cd = new ConnectionCheck(getApplicationContext());
        Timer t = new Timer();
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            t.schedule(new splash(),3000);
        } else {
            Toast.makeText(SplashScreen.this,"connection not found\ncheck your connection",Toast.LENGTH_LONG).show();
            t.schedule(new splash(),3000);
        }
    }

    class splash extends TimerTask {
        @Override
        public void run() {
            Intent i = new Intent(SplashScreen.this,Login.class);
            finish();
            startActivity(i);
        }

    }
}
