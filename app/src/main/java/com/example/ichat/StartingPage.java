package com.example.ichat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class StartingPage extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_page);

        new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
                Intent homeIntent = new Intent(StartingPage.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
                }
            },SPLASH_TIME_OUT);
    }
}
