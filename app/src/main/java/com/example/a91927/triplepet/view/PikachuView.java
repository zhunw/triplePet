package com.example.a91927.triplepet.view;

import android.animation.ValueAnimator;
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
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.example.a91927.triplepet.R;
import com.example.a91927.triplepet.util.PetAlphaEvaluator;
import com.example.a91927.triplepet.util.PetAlphaValue;
import com.example.a91927.triplepet.util.PetSizeEvaluator;
import com.example.a91927.triplepet.util.PetSizeValue;

import static java.lang.Math.min;

public class PikachuView extends BasePetView {
    ValueAnimator animAlpha = new ValueAnimator();
    ValueAnimator animSize = new ValueAnimator();
    PetAlphaValue currentPetAlphaVal = new PetAlphaValue(1.0f);
    PetSizeValue currentPetSizeVal = new PetSizeValue(init_size, init_size);
    /* ************************** */
    public PikachuView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        if(currentPetAlphaVal != null)
            Log.i("log", "currentPetVal not null");
        initValues();
//        W = 100;
//        H = 100;
//        x = 100;
//        y = 100;
        setFocusable(true);
        //sharedPreferences = context.getSharedPreferences("com.tencent.xidian.ourpet_preferences",Context.MODE_PRIVATE);
//        SharedPreferences sp = context.getSharedPreferences(context.getString(R.string.our_pets_settings), Context.MODE_PRIVATE);
        res = context.getResources();
        measureScreen();
        Log.i("log", String.format("w:%d, h:%d", screenW, screenH));
//        init(sp);
        initBmp();
        //test
        bmp_tmp = BitmapFactory.decodeResource(res, R.drawable.pika_largest);
    }
    public PikachuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PikachuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /* **************************** */
    protected void initBmp() {
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
    protected void initValues() {
        W = (int)currentPetSizeVal.W;
        H = (int)currentPetSizeVal.H;
        x = init_size;
        y = init_size;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        //alpha anim
        paint.setAlpha((int)(currentPetAlphaVal.alpha * 255));
        //size anim
        W = (int)currentPetSizeVal.W;
        H = (int)currentPetSizeVal.H;
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
            case ONBOOM:
                if(bmp_tmp != null)
                    drawedBitmap = bmp_tmp;
                else
                    drawedBitmap = bmpArray[0];
                break;
        }

//        Log.i("idx", Integer.toString(idx));
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sheep);
        int bmpW = drawedBitmap.getWidth();
        int bmpH = drawedBitmap.getHeight();
        Rect blgRecS = new Rect(0, 0, bmpW, bmpH);
        Rect blgRecD = new Rect(0, 0, W, H);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
        canvas.drawBitmap(drawedBitmap, blgRecS, blgRecD, paint);
        canvas.drawRect(0, 0, W/4, H/4, paint);
        canvas.drawRect(3*W/4, 3*H/4, W, H, paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if(untouchable)
            return true;
        touchX = event.getRawX();
        touchY = event.getRawY();
        //for special ainm
        touchinx = -1;
        touchiny = -1;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Log.i("log", String.format("touch %d %d", touchinx, touchiny));
                if(pet_state == PET_STATE.ONBOOM) {
                    currentPetSizeVal.W = init_size;
                    currentPetSizeVal.H = init_size;
                    pet_state = PET_STATE.NORMAL;
                }
                touchDownTime = System.currentTimeMillis();
                touchinx = event.getX();
                touchiny = event.getY();
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
                //clean
                touchAnimAlpha = false;
                touchAnimSize = false;
                if(onPressing) onPressing = false;
                titleBarH = 0;
                diffTime = System.currentTimeMillis() - touchDownTime;
                Log.i("log", String.format("%d", diffTime));

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
//        diffTime = System.currentTimeMillis() - touchDownTime;

        if( diffTime < 400 ) {
            if(touchinx >= 0 && touchinx < W/4 && touchiny < H/4 && touchiny >= 0 )
                touchAnimAlpha = true;
            if(touchinx > 3*W/4 && touchiny > 3*H/4 )
                touchAnimSize = true;
        }
        //
        return true;
    }
    //anim
    public void startAlphaAnimation() {
        PetAlphaValue startVal = new PetAlphaValue(1.0f);
        PetAlphaValue endVal = new PetAlphaValue(0.0f);
        animAlpha = ValueAnimator.ofObject(new PetAlphaEvaluator(), startVal, endVal);
        animAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPetAlphaVal = (PetAlphaValue) animation.getAnimatedValue();
                invalidate();
            }
        });
        animAlpha.setDuration(2000);
//        animAlpha.setRepeatCount(Animation.INFINITE);
        animAlpha.setRepeatCount(Animation.ABSOLUTE);
        animAlpha.setInterpolator(new LinearInterpolator());//设置插值器
        animAlpha.start();
    }
    public void startSizeAnimation() {
        float rate = boom_rate;
        float tmpw = W * rate;
        float tmph = H * rate;
        if (tmpw > screenW) tmpw = screenW;
        if (tmph > screenH) tmph = screenH;
        float tmps = min(tmph, tmpw);
        tmph = tmpw = tmps;
        PetSizeValue startVal = new PetSizeValue(init_size, init_size);
        PetSizeValue endVal = new PetSizeValue(tmpw, tmph);
        animSize = ValueAnimator.ofObject(new PetSizeEvaluator(), startVal, endVal);
        animSize.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPetSizeVal = (PetSizeValue) animation.getAnimatedValue();
                invalidate();
            }
        });
        animSize.setDuration(2000);
//        animSize.setRepeatCount(Animation.INFINITE);
        animSize.setRepeatCount(Animation.ABSOLUTE);
        animSize.setInterpolator(new LinearInterpolator());//设置插值器
        animSize.start();
    }
    public void stopAnimation() {
//        anim.end();
//        anim.cancel();
        animAlpha.pause();
        //clean
        touchAnimAlpha = false;
        touchinx = -1;
        touchiny = -1;
        currentPetAlphaVal.alpha =1.0f;
    }
    public void stopSizeAnimation() {
        animSize.pause();
        touchAnimSize = false;
        touchinx = -1;
        touchiny = -1;
//        currentPetSizeVal.W = init_size;
//        currentPetSizeVal.H = init_size;
        pet_state = PET_STATE.ONBOOM;
    }

}
