package com.example.a91927.triplepet.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.a91927.triplepet.R;
import com.example.a91927.triplepet.util.NotiReceiver;
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
    DisturbRunnable disturbRunnable = new DisturbRunnable();
    Handler handler;
    //noti
    Notification notification;
    NotificationManager notiManager;
    int myNotiId = 33333;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        petView = initPetView();
        if(petView != null)
        Log.i("log", "petView not null");
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        petView.setBackgroundColor(Color.TRANSPARENT);
        initLayoutParams();
        windowManager.addView(petView, layoutParams);// 这句是重点 给WindowManager中丢入刚才设置的值
        handler = new Handler();
        handler.post(drawRunnable);
        handler.post(disturbRunnable);
        //noti
        initNotification();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        notiManager.cancel(myNotiId);
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
        layoutParams.type = 2002;
//        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
//        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layoutParams.flags = 40;// 这句设置桌面可控
        layoutParams.x = (int)petView.getX();
        layoutParams.y = (int)petView.getY();
        layoutParams.width = petView.getW();
        layoutParams.height = petView.getH();
//        layoutParams.format = -3; // 透明
    }
    void initNotification() {
        notification = new Notification();
        notiManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notiIntent = new Intent();
        notiIntent.setAction("MyNotiReceive");
        notiIntent.putExtra("name1", "Pikachu");
        notiIntent.putExtra("number", 33.0f);
        PendingIntent pdintent = PendingIntent.getBroadcast(
                this, 0, notiIntent, 0);
        notification.defaults = Notification.DEFAULT_SOUND;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notification.tickerText = "我的宠物";
        notification.contentIntent = pdintent;
        notification.icon = R.drawable.pika_largest;
        notification.when = System.currentTimeMillis();
        notification.contentView = new RemoteViews(getPackageName(),
                R.layout.noti);
        notification.contentView.setImageViewResource(R.id.iv_pet, R.drawable.pika_largest);
        notification.contentView.setTextViewText(R.id.tv_hello, "Pikachu");
        notiManager.notify(myNotiId, notification);
        //
        NotiReceiver notiReceiver = new NotiReceiver();
        IntentFilter inf = new IntentFilter();
        inf.addAction("MyNotiReceive");
        this.registerReceiver(notiReceiver, inf);
    }

    class DrawRunnable implements Runnable {
        @Override
        public void run() {
            try {
                if(petView.getDiffTime() > 4000) {
                    windowManager.removeViewImmediate(petView);
                    return;
                }
                else if(petView.getTouchAnimAlpha()) {
                    petView.startAlphaAnimation();
                    petView.setUntouchable(true);
                }
                else if(petView.getTouchAnimSize()) {
                    petView.startSizeAnimation();
                    petView.setUntouchable(true);
                }
//                else {
                    int x = (int)petView.getX();
                    int y = (int)petView.getY();
                    layoutParams.x = x;
                    layoutParams.y = y;
                    //
                    int w = petView.getW();
                    int h = petView.getH();
                    layoutParams.width = w;
                    layoutParams.height = h;
//                Log.i("log", String.format("size:%d %d", w, h));
                    petView.invalidate();
                    windowManager.updateViewLayout(petView, layoutParams);
//                }
//                if(petView.getOnPressing())
                handler.post(drawRunnable);
//                    handler.postAtFrontOfQueue(drawRunnable);
//                else
//                    handler.postDelayed(drawRunnable, 100);
//            windowManager.removeViewImmediate(petView);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class DisturbRunnable implements Runnable {
        @Override
        public void run() {
            try {
                petView.setIdx((petView.getIdx() + 1) % petView.getNumOfBmp() );
                handler.postDelayed(disturbRunnable , 500);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
