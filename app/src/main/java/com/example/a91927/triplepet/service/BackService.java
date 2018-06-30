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
import com.example.a91927.triplepet.view.BasePetView;
import com.example.a91927.triplepet.view.PikachuView;

import static java.lang.Thread.sleep;

/**
 * Created by 91927 on 2018/6/21.
 */

public class BackService extends Service {
    Context context;
    WindowManager windowManager;
    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
    BasePetView petView;
    String petType = "Pikachu";
    DrawRunnable drawRunnable = new DrawRunnable();
    Handler handler;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        petView = initPetView();
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        petView.setBackgroundColor(Color.TRANSPARENT);
        initLayoutParams();
        windowManager.addView(petView, layoutParams);// 这句是重点 给WindowManager中丢入刚才设置的值
        handler = new Handler();
        handler.post(drawRunnable);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    BasePetView initPetView() {
        BasePetView petView;
        switch(petType) {
            case "Pikachu":
                petView = new PikachuView(this);
                break;
            default:
                petView = new PikachuView(this);
                break;
        }
        return petView;
    }
    void initLayoutParams() {
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT|Gravity.TOP;
        layoutParams.type = 2002; // type是关键，这里的2002表示系统级窗口，或2003。
        layoutParams.flags = 40;// 这句设置桌面可控
        layoutParams.x = (int)petView.getX();
        layoutParams.y = (int)petView.getY();
        layoutParams.width = petView.getW();
        layoutParams.height = petView.getH();
        layoutParams.format = -3; // 透明
    }

    class DrawRunnable implements Runnable {
        @Override
        public void run() {
            try {
                int x = (int)petView.getX();
                int y = (int)petView.getY();
                layoutParams.x = x;
                layoutParams.y = y;
//                Log.i("posi", String.format("bmp:%d %d", x, y));
//                Log.i("posi", String.format("touch:%d %d", (int)petView.touchX, (int)petView.touchY));
                petView.invalidate();
                windowManager.updateViewLayout(petView, layoutParams);
                if(petView.getOnPressing())
                    handler.post(drawRunnable);
                else
                    handler.postDelayed(drawRunnable, 100);
//            windowManager.removeViewImmediate(petView);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
