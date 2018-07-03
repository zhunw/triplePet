package com.example.a91927.triplepet.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.example.a91927.triplepet.R;
import com.example.a91927.triplepet.util.PetAlphaEvaluator;
import com.example.a91927.triplepet.util.PetAlphaValue;
import com.example.a91927.triplepet.util.PetPosiEvaluator;
import com.example.a91927.triplepet.util.PetPosiValue;
import com.example.a91927.triplepet.util.PetProgressEvaluator;
import com.example.a91927.triplepet.util.PetProgressValue;
import com.example.a91927.triplepet.util.PetSizeEvaluator;
import com.example.a91927.triplepet.util.PetSizeValue;

import com.example.a91927.triplepet.R;

import java.util.Random;

public class ChopperView extends BasePetView {
    Bitmap[] bmpR2LAnimArray;
    final int numOfToLeftAnim = 3;
    Bitmap[] bmpEatAnimArray;
    final int numOfEatAnim = 4;
    Bitmap[] bmpSleepAnimArray;
    final int numOfSleepAnim = 7;

    enum CHOPPER_STATE {BASE, EAT, SLEEP};
    CHOPPER_STATE chopper_state = CHOPPER_STATE.BASE;
    PetProgressValue currentPetProgressVal = new PetProgressValue(0f);

    public ChopperView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        setFocusable(true);
        res = context.getResources();
        measureScreen();
        initValues();
        delayTime = 1000;
        Log.i("log", String.format("w:%d, h:%d", screenW, screenH));
        Log.i("log", String.format("W:%d, H:%d", W, H));
        initBmp();
        initToLeftBmp();
        initEatBmp();
        initSleepBmp();
    }
    @Override
    protected void initBmp() {
        numOfBmp = 3;
        bmpArray = new Bitmap[numOfBmp];
        String str = "chopper_happy_";
        for(int i = 1; i <= numOfBmp; i++) {
            String name = str + Integer.toString(i);
            bmpArray[i-1] = decodeResource(res, getRawID(name));
        }
        hide_left = BitmapFactory.decodeResource(res, R.drawable.hide_left);
        hide_right = BitmapFactory.decodeResource(res, R.drawable.hide_right);
        bmp_tmp = BitmapFactory.decodeResource(res, R.raw.chopper_sleep_6);
    }
    private void initToLeftBmp() {
        bmpR2LAnimArray = new Bitmap[numOfToLeftAnim];
        String str = "chopper_walk_";
        for(int i = 1; i <= numOfToLeftAnim; i++) {
            String name = str + Integer.toString(i);
            bmpR2LAnimArray[i-1] = decodeResource(res, getRawID(name));
        }
    }
    private void initEatBmp() {
        bmpEatAnimArray = new Bitmap[numOfEatAnim];
        String str = "chopper_eat_";
        for(int i = 1; i <= numOfEatAnim; i++) {
            String name = str + Integer.toString(i);
            bmpEatAnimArray[i-1] = decodeResource(res, getRawID(name));
        }
    }
    private void initSleepBmp() {
        bmpSleepAnimArray = new Bitmap[numOfSleepAnim];
        String str = "chopper_sleep_";
        for(int i = 1; i <= numOfSleepAnim; i++) {
            String name = str + Integer.toString(i);
            bmpSleepAnimArray[i-1] = decodeResource(res, getRawID(name));
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAlpha((int)(currentPetAlphaVal.alpha * 255));
        W = (int)currentPetSizeVal.W;
        H = (int)currentPetSizeVal.H;
        int tmpidx = 0;
        float single_ = 100f;
        float progress;
        switch(pet_state) {
            case NORMAL:
                drawedBitmap = bmpArray[idx];
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
            case R2L:
                x = currentPetPosiVal.x;
                y = currentPetPosiVal.y;
                single_ = (screenW-W)/(float)numOfToLeftAnim/3; //每个区间长度
                tmpidx = (int)(x/single_);
                tmpidx = tmpidx % numOfToLeftAnim;
                drawedBitmap = bmpR2LAnimArray[tmpidx];
                break;
            case PRIVATE:
                switch(chopper_state) {
                    case BASE:
                        drawedBitmap = bmpArray[idx];
                        break;
                    case EAT:
                        progress = currentPetProgressVal.progress;
                        single_ = (100f)/(float)numOfEatAnim; //每个区间长度
                        tmpidx = (int)(progress/single_);
                        tmpidx = tmpidx % numOfEatAnim;
                        Log.i("log", "tmpidx"+Integer.toString(tmpidx));
                        drawedBitmap = bmpEatAnimArray[tmpidx];
                        break;
                    case SLEEP:
                        progress = currentPetProgressVal.progress;
                        single_ = (100f)/(float)numOfSleepAnim; //每个区间长度
                        tmpidx = (int)(progress/single_);
                        tmpidx = tmpidx % numOfSleepAnim;
                        drawedBitmap = bmpSleepAnimArray[tmpidx];
                        break;
                }
        }

        int bmpW = drawedBitmap.getWidth();
        int bmpH = drawedBitmap.getHeight();
        Rect blgRecS = new Rect(0, 0, bmpW, bmpH);
        Rect blgRecD = new Rect(0, 0, W, H);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
        canvas.drawBitmap(drawedBitmap, blgRecS, blgRecD, paint);
        Paint rectp = new Paint();
        rectp.setColor(Color.GREEN);
        rectp.setAlpha((int)(50));
        canvas.drawRect(0, 0, W/4, H/4, rectp);
        canvas.drawRect(3*W/4, 3*H/4, W, H, rectp);
        canvas.drawRect(0, 3*H/4, W/4, H, rectp);
        canvas.drawRect(3*W/4, 0, W, H/4, rectp);
        canvas.drawRect(0, 3.0f/8*H, W/4, 5.0f/8*H, rectp);
    }
    public boolean onTouchEvent(MotionEvent event) {
        if(untouchable)
            return true;
        touchX = event.getRawX();
        touchY = event.getRawY();
        touchinx = event.getX();
        touchiny = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(touchinx >= 0 && touchinx < W/4 && touchiny < H/4 && touchiny >= 0 ) //up-left
                    startAlphaAnimation();
                if(touchinx > 3*W/4 && touchiny > 3*H/4 ) //down-right
                    startEatAnimation();
                if(touchinx >= 0 && touchinx < W/4 && touchiny > 3*H/4 ) //down-left
                    startSleepAnimation();
                if(touchinx > 3*W/4 && touchiny < H/4 && touchiny >= 0 ) //up-right
                    startSizeAnimation();
                if(touchinx >= 0 && touchinx < W/4 && touchiny < H*5.0f/8 && touchiny >= 3.0f/8*H)
                    startR2LAnimation();
//                Log.i("log", String.format("touch %d %d", touchinx, touchiny));
                if(pet_state == PET_STATE.ONBOOM) {
                    currentPetSizeVal.W = init_width;
                    currentPetSizeVal.H = init_height;
                    pet_state = PET_STATE.NORMAL;
                }
                touchDownTime = System.currentTimeMillis();
                if(!onPressing) onPressing = true;
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
                else if(pet_state != PET_STATE.ONBOOM && pet_state != PET_STATE.L2R
                        && pet_state!=PET_STATE.R2L && pet_state!=PET_STATE.PRIVATE)
                    pet_state = PET_STATE.NORMAL;

                break;
        }
        return true;
    }
    void startEatAnimation() {
        setUntouchable(true);
        pet_state = PET_STATE.PRIVATE;
        chopper_state = CHOPPER_STATE.EAT;
        PetProgressValue startVal = new PetProgressValue(0f);
        PetProgressValue endVal = new PetProgressValue(100f);
        distance = 100f - 0f;
        animAlpha = ValueAnimator.ofObject(new PetProgressEvaluator(), startVal, endVal);
        animAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPetProgressVal = (PetProgressValue) animation.getAnimatedValue();
                invalidate();
            }
        });
        animAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentPetProgressVal.progress = 0.0f;
                pet_state = PET_STATE.NORMAL;
                chopper_state = CHOPPER_STATE.BASE;
                setUntouchable(false);
            }
        });
        animAlpha.setDuration(2000);
        animAlpha.setRepeatCount(Animation.ABSOLUTE);
        animAlpha.setInterpolator(new LinearInterpolator());//设置插值器
        animAlpha.start();
    }
    public void startR2LAnimation() {
        tmpx = x;
        setUntouchable(true);
        pet_state = PET_STATE.R2L;
        float fromx = x, tox = 0;
        Random r = new Random();
        int step = r.nextInt(10);
        step = 5-step;
        float fromy = y, toy = y + (step*screenH/10);
        if(toy < 0) toy = 0;
        if(toy > screenH-H) toy = screenH-H;
        float distance = screenW - W;
        int dura = (int)(x/distance * 3000);
        PetPosiValue startVal = new PetPosiValue(fromx, fromy);
        PetPosiValue endVal = new PetPosiValue(tox, toy);
        animPosi = ValueAnimator.ofObject(new PetPosiEvaluator(), startVal, endVal);
        animPosi.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPetPosiVal = (PetPosiValue) animation.getAnimatedValue();
                invalidate();
            }
        });
        animPosi.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                pet_state = PET_STATE.HIDE_LEFT;
                chopper_state = CHOPPER_STATE.BASE;
                setUntouchable(false);
            }
        });
        animPosi.setDuration(dura);
        animPosi.setRepeatCount(Animation.ABSOLUTE);
        animPosi.setInterpolator(new LinearInterpolator());//设置插值器
        animPosi.start();
    }
    public void startSleepAnimation() {
        setUntouchable(true);
        pet_state = PET_STATE.PRIVATE;
        chopper_state = CHOPPER_STATE.SLEEP;
        PetProgressValue startVal = new PetProgressValue(0f);
        PetProgressValue endVal = new PetProgressValue(100f);
        distance = 100f - 0f;
        animAlpha = ValueAnimator.ofObject(new PetProgressEvaluator(), startVal, endVal);
        animAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPetProgressVal = (PetProgressValue) animation.getAnimatedValue();
                invalidate();
            }
        });
        animAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentPetProgressVal.progress = 0.0f;
                pet_state = PET_STATE.NORMAL;
                chopper_state = CHOPPER_STATE.BASE;
                setUntouchable(false);
            }
        });
        animAlpha.setDuration(2000);
        animAlpha.setRepeatCount(Animation.ABSOLUTE);
        animAlpha.setInterpolator(new LinearInterpolator());//设置插值器
        animAlpha.start();
    }
}
