package com.example.a91927.triplepet.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.a91927.triplepet.R;

public class PikachuView extends BasePetView {
    /* ************************** */
    public PikachuView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        W = H = 300;
        x = 100;
        y = 100;
        setFocusable(true);
        //sharedPreferences = context.getSharedPreferences("com.tencent.xidian.ourpet_preferences",Context.MODE_PRIVATE);
//        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.our_pets_settings), Context.MODE_PRIVATE);
        res = context.getResources();
        measureScreen();
        Log.i("screen", String.format("w:%d, h:%d", screenW, screenH));
//        init(sp);
        initBmp();
        //test

    }
    public PikachuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PikachuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /* **************************** */
    public void initBmp() {
        numOfBmp = 5;
        bmpArray = new Bitmap[numOfBmp];
        String str = "stand_";
        for(int i = 1; i <= numOfBmp; i++) {
            String name = str + Integer.toString(i);// + ".png";
            bmpArray[i-1] = decodeResource(res, getRawID(name));
        }
        hide_left = BitmapFactory.decodeResource(res, R.drawable.hide_left);
        hide_right = BitmapFactory.decodeResource(res, R.drawable.hide_right);
//        bmpArray[0] = BitmapFactory.decodeResource(getResources(), R.raw.stand_1);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        switch(pet_state) {
            case NORMAL:
                if(onPressing) {
                    drawedBitmap = bmpArray[0];
                }
                else {
                    drawedBitmap = bmpArray[idx];
//                    idx = (idx+1) % numOfBmp;
                }
                break;
            case HIDE_LEFT:
                if(hide_left != null)
                    drawedBitmap = hide_left;
                else //for secure
                    drawedBitmap = bmpArray[0];
                break;
            case HIDE_RIGHT:
                if(hide_right != null)
                    drawedBitmap = hide_right;
                else //for secure
                    drawedBitmap = bmpArray[0];
                break;
        }

        Log.i("idx", Integer.toString(idx));
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sheep);
        int bmpW = drawedBitmap.getWidth();
        int bmpH = drawedBitmap.getHeight();
        Rect blgRecS = new Rect(0, 0, bmpW, bmpH);
        Rect blgRecD = new Rect(0, 0, W, H);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
        canvas.drawBitmap(drawedBitmap, blgRecS, blgRecD, paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getRawX();
        touchY = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchDownTime = System.currentTimeMillis();
                if(!onPressing) onPressing = true;
//                onActionChange(FLAG_UP);
                titleBarH = touchY - event.getY() - y;
                break;
            case MotionEvent.ACTION_MOVE:
                if(!onPressing) onPressing = true;
                x = touchX - W/2;
                y = touchY - H/2 - titleBarH;
                break;
            case MotionEvent.ACTION_UP:
                if(onPressing) onPressing = false;
                titleBarH = 0;
                diffTime = System.currentTimeMillis() - touchDownTime;
                // 贴边
                if(x < 50) {
                    x = 0;
                    pet_state = PET_STATE.HIDE_LEFT;
                }
                else if(screenW - W - x < 50) {
                    x = screenW - W;
                    pet_state = PET_STATE.HIDE_RIGHT;
                }
                else
                    pet_state = PET_STATE.NORMAL;
                break;
        }
        //clean
        touchAnimAlpha = false;
        //for special ainm
        float touchx = event.getX(), touchy = event.getY();
        if(x < W/2 && y < H/2) {
            touchAnimAlpha = true;
        }
        //
        return true;
    }


}
