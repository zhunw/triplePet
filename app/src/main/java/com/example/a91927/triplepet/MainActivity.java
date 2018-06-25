package com.example.a91927.triplepet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.a91927.triplepet.service.BackService;

public class MainActivity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickFun();
            }
        });
    }

    public void clickFun() {
        Intent intent = new Intent(this.getApplicationContext(), BackService.class);
        this.startService(intent);
    }
}
