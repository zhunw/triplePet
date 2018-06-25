package com.example.a91927.triplepet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.a91927.triplepet.view.PetView;

/**
 * Created by 91927 on 2018/6/21.
 */

public class BackService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        //new view
        PetView petView = new PetView(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
