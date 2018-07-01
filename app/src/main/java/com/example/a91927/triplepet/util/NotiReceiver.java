package com.example.a91927.triplepet.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.a91927.triplepet.service.BackService;

public class NotiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent real = intent.getParcelableExtra("real");
//        Intent real = intent;
//        String str = real.getStringExtra("name1");
//        float f = real.getFloatExtra("number", 100f);
//        String s1 = Float.toString(f);
        Toast.makeText(context, "push", Toast.LENGTH_SHORT).show();
    }
}
