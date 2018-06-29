package com.example.a91927.triplepet.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.a91927.triplepet.R;
import com.example.a91927.triplepet.view.PetView;

import static java.lang.Thread.sleep;

/**
 * Created by 91927 on 2018/6/21.
 */

public class BackService extends Service {
    Context context;
    WindowManager mWM; // WindowManager
    WindowManager.LayoutParams layoutParams; // WindowManager参数
    PetView pet;
    DrawRunnable drawRunnable;
    Handler handler;
    int r = 0;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        pet = new PetView(this);
        drawRunnable = new DrawRunnable();
//        pet =  LayoutInflater.from(this).inflate(R.layout.pet_layout, null);
        mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        pet.setBackgroundColor(Color.TRANSPARENT);
        layoutParams.type=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT|Gravity.TOP;
        layoutParams.type = 2002; // type是关键，这里的2002表示系统级窗口，或2003。
        layoutParams.flags = 40;// 这句设置桌面可控
        layoutParams.x = (int)pet.x;
        layoutParams.y = (int)pet.y;
//        layoutParams.x = -100;
//        layoutParams.y = 0;
        layoutParams.width = 100;
        layoutParams.height = 100;
        layoutParams.format = 128; // 透明
        mWM.addView(pet, layoutParams);// 这句是重点 给WindowManager中丢入刚才设置的值
        handler = new Handler();
        handler.post(drawRunnable);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    class DrawRunnable implements Runnable {
        @Override
        public void run() {
//            pet.invalidate();
//            int xx[] = {100, 200, 300, 400};
//            int yy[] = {100, 200, 300, 400};
//            r = (r+1) % 4;
            try {
//                sleep(1000);
                int x = (int)pet.x;
                int y = (int)pet.y;
                layoutParams.x = x;
                layoutParams.y = y;
                Log.i("posi", String.format("bmp:%d %d", x, y));
//                Log.i("posi", String.format("touch:%d %d", (int)pet.touchX, (int)pet.touchY));
                pet.invalidate();
                mWM.updateViewLayout(pet, layoutParams);
                handler.post(drawRunnable);
//            mWM.removeViewImmediate(pet);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
