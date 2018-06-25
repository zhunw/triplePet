package com.example.a91927.triplepet.view;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.a91927.triplepet.R;

/**
 * Created by 91927 on 2018/6/21.
 */

public class PetView extends LinearLayout {
    Context c;
    WindowManager mWM; // WindowManager
    WindowManager.LayoutParams mWMParams; // WindowManager参数
    View win;
    int tag = 0;
    int oldOffsetX;
    int oldOffsetY;
    public PetView(Context context) {
        super(context);
        c = context;
        fun();
    }

    public void fun() {
// 设置载入view WindowManager参数
        mWM = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        win = LayoutInflater.from(c).inflate(R.layout.pet_layout, this);
        win.setBackgroundColor(Color.TRANSPARENT);
        // 这里是随便载入的一个布局文件
        win.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float lastX = 0, lastY = 0;
                final int action = motionEvent.getAction();
                float x = motionEvent.getX();
                float y = motionEvent.getY();
                if(tag == 0){
                    oldOffsetX= mWMParams.x; // 偏移量
                    oldOffsetY = mWMParams.y; // 偏移量
                }
                if (action == MotionEvent.ACTION_DOWN) {
                    lastX = x;
                    lastY = y;
                }
                else if (action == MotionEvent.ACTION_MOVE) {
                    mWMParams.x += (int) (x - lastX); // 偏移量
                    mWMParams.y += (int) (y - lastY); // 偏移量
                    tag = 1;
                    mWM.updateViewLayout(win, mWMParams);
                }
                else if (action == MotionEvent.ACTION_UP){
                    int newOffsetX = mWMParams.x; int newOffsetY = mWMParams.y;
                    if(oldOffsetX == newOffsetX && oldOffsetY == newOffsetY){
                        Toast.makeText(c, "你点到我了……疼！！！！ ", Toast.LENGTH_SHORT).show();
                    }else {
                        tag = 0;
                    }
                }
                return true;
            }
        });
        WindowManager wm = mWM;
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        mWMParams = wmParams;
        wmParams.type = 2002; // type是关键，这里的2002表示系统级窗口，或2003。
        wmParams.flags = 40;// 这句设置桌面可控
        wmParams.width = 300;
        wmParams.height = 300;
        wmParams.format = 255; // 透明
        wm.addView(win, wmParams);// 这句是重点 给WindowManager中丢入刚才设置的值
        // 只有addview后才能显示到页面上去。
        // 注册到WindowManager win是要刚才随便载入的layout，
        //wmParams是刚才设置的WindowManager参数集
        // 效果是将win注册到WindowManager中并且它的参数是wmParams中设置
    }

}
