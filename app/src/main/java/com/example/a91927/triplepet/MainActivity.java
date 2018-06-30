package com.example.a91927.triplepet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.a91927.triplepet.service.BackService;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startIntent();
    }

    public void startIntent() {
        Intent intent = new Intent(this.getApplicationContext(), BackService.class);
        this.startService(intent);
    }
}
