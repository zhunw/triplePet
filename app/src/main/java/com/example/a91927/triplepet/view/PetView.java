package com.example.a91927.triplepet.view;

import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
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

public class PetView extends View {
    int tag = 0;
    public float x = 100, y = 100;
    protected float personSize = 0.4f;
    /**人物图片的宽高*/
    protected int bmpW, bmpH;
    /**当前系统标题栏的高度，如果全屏则为0*/
    protected float titleBarH;
    /**触摸时的坐标*/
    public float touchX, touchY;
    /**当move时，前一个touchX坐标（标识左右移动）*/
    protected float touchPreX;
    int lastX, lastY;
    int paramX, paramY;
    int onPerson;
    public int W, H;
    public PetView(Context context) {
        super(context);
    }
    public PetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawLine(0, 0, 50, 50, paint);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sheep);
        bmpW = bitmap.getWidth();
        bmpH = bitmap.getHeight();
        Rect blgRecS = new Rect(0, 0, bmpW, bmpH);
        W = 100;
        H = 100;
        Rect blgRecD = new Rect(0, 0, W, H);
        canvas.drawBitmap(bitmap, blgRecS, blgRecD, paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getRawX();
        touchY = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                touchDownTime = System.currentTimeMillis();
                if( !(onPerson == -1) )onPerson = 1;
//                onActionChange(FLAG_UP);
                touchPreX = touchX;
                titleBarH = touchY - event.getY() - y;
                break;
            case MotionEvent.ACTION_MOVE:
                if( !(onPerson == -1) )onPerson = 0;
                //触摸点的显示作用
//                int dx = (int) event.getRawX() - lastX;
//                int dy = (int) event.getRawY() - lastY;
//                x += dx;
//                y += dy;
                x = touchX - W/2;
                y = touchY - H/10 - titleBarH-50;
                break;
            case MotionEvent.ACTION_UP:
//                if( !(onPerson == -1) )onPerson = 0;
                //报时的绘制点
//                x += 10;
                titleBarH = 0;
//                Toast.makeText(getContext(), "here", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
//    public void fun() {
// 设置载入view WindowManager参数

//        win = new PetView(c);
//        win = LayoutInflater.from(c).inflate(R.layout.pet_layout, null);
//        win = LayoutInflater.from(c).inflate
//        win.setBackgroundColor(Color.TRANSPARENT);
        // 这里是随便载入的一个布局文件;
//        public boolean onTouchEvent(MotionEvent event){
//            float lastX = 0, lastY = 0;
//            final int action = motionEvent.getAction();
//            float x = motionEvent.getX();
//            float y = motionEvent.getY();
//            if(view.tag == 0){
//                oldOffsetX= mWMParams.x; // 偏移量
//                oldOffsetY = mWMParams.y; // 偏移量
//            }
//            if (action == MotionEvent.ACTION_DOWN) {
//                lastX = x;
//                lastY = y;
//            }
//            else if (action == MotionEvent.ACTION_MOVE) {
//                mWMParams.x += (int) (x - lastX); // 偏移量
//                mWMParams.y += (int) (y - lastY); // 偏移量
//                tag = 1;
//                mWM.updateViewLayout(win, mWMParams);
//            }
//            else if (action == MotionEvent.ACTION_UP){
//                int newOffsetX = mWMParams.x; int newOffsetY = mWMParams.y;
//                if(oldOffsetX == newOffsetX && oldOffsetY == newOffsetY){
//                    Toast.makeText(c, "你点到我了……疼！！！！ ", Toast.LENGTH_SHORT).show();
//                }else {
//                    tag = 0;
//                }
//            }
//            return true;
//        }
//    });
//    WindowManager wm = mWM;
//    WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
//    mWMParams = wmParams;
//    wmParams.type = 2002; // type是关键，这里的2002表示系统级窗口，或2003。
//    wmParams.flags = 40;// 这句设置桌面可控
//    wmParams.width = 300;
//    wmParams.height = 300;
//    wmParams.format = 255; // 透明
//        wm.addView(win, wmParams);// 这句是重点 给WindowManager中丢入刚才设置的值
    // 只有addview后才能显示到页面上去。
    // 注册到WindowManager win是要刚才随便载入的layout，
    //wmParams是刚才设置的WindowManager参数集
    // 效果是将win注册到WindowManager中并且它的参数是wmParams中设置
//    }

}
