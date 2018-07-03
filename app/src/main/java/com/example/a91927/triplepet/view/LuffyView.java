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

/**
 * Created by 91927 on 2018/7/3.
 */

public class LuffyView extends BasePetView {
    Bitmap[] bmpL2RAnimArray;
    final int numOfToRightAnim = 3;
    Bitmap[] bmpR2LAnimArray;
    final int numOfToLeftAnim = 2;
    Bitmap[] bmpFallAnimArray;
    final int numOfFallAnim = 2;
    PetPosiValue currentPetFallVal = new PetPosiValue(0f, 0f);
    float fallStartX;

    public LuffyView(Context context) {
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
        initToRightBmp();
        initToLeftBmp();
        initFallBmp();
    }
    @Override
    protected void initBmp() {
        numOfBmp = 5;
        bmpArray = new Bitmap[numOfBmp];
        String str = "luffy_eat_";
        for(int i = 1; i <= numOfBmp; i++) {
            String name = str + Integer.toString(i);// + ".png";
            bmpArray[i-1] = decodeResource(res, getRawID(name));
        }
        hide_left = BitmapFactory.decodeResource(res, R.drawable.hide_left);
        hide_right = BitmapFactory.decodeResource(res, R.drawable.hide_right);
        bmp_tmp = BitmapFactory.decodeResource(res, R.raw.luffy_right_1);
    }
    private void initToRightBmp() {
        bmpL2RAnimArray = new Bitmap[numOfToRightAnim];
        String str = "luffy_right_";
        for(int i = 1; i <= numOfToRightAnim; i++) {
            String name = str + Integer.toString(i);
            bmpL2RAnimArray[i-1] = decodeResource(res, getRawID(name));
        }
    }
    private void initToLeftBmp() {
        bmpR2LAnimArray = new Bitmap[numOfToLeftAnim];
        String str = "luffy_left_";
        for(int i = 1; i <= numOfToLeftAnim; i++) {
            String name = str + Integer.toString(i);
            bmpR2LAnimArray[i-1] = decodeResource(res, getRawID(name));
        }
    }
    private void initFallBmp() {
        bmpFallAnimArray = new Bitmap[numOfFallAnim];
        String str = "luffy_fall_";
        for(int i = 1; i <= numOfFallAnim; i++) {
            String name = str + Integer.toString(i);
            bmpFallAnimArray[i-1] = decodeResource(res, getRawID(name));
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
        switch(pet_state) {
            case NORMAL:
                if(onPressing) {
                    drawedBitmap = bmpArray[0];
                }
                else {
                    drawedBitmap = bmpArray[idx];
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
            case L2R:
                x = currentPetPosiVal.x;
                y = currentPetPosiVal.y;
                float single = (screenW-W)/(float)numOfToRightAnim/3; //每个区间长度
                tmpidx = (int)(x/single);
                tmpidx = (tmpidx % numOfToRightAnim);
                drawedBitmap = bmpL2RAnimArray[tmpidx];
                break;
            case R2L:
                x = currentPetPosiVal.x;
                y = currentPetPosiVal.y;
                float single_ = (screenW-W)/(float)numOfToLeftAnim/3; //每个区间长度
                tmpidx = (int)(x/single_);
                tmpidx = tmpidx % numOfToLeftAnim;
                drawedBitmap = bmpR2LAnimArray[tmpidx];
                break;
            case PRIVATE:
                x = currentPetFallVal.x;
                single_ = (distance)/(float)numOfFallAnim; //每个区间长度
                tmpidx = (int)((fallStartX-x)/single_);
                tmpidx = (tmpidx) % bmpFallAnimArray.length;
                drawedBitmap = bmpFallAnimArray[tmpidx];
                break;
        }

        int bmpW = drawedBitmap.getWidth();
        int bmpH = drawedBitmap.getHeight();
        Rect blgRecS = new Rect(0, 0, bmpW, bmpH);
        Rect blgRecD = new Rect(0, 0, W, H);
//        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
        canvas.drawBitmap(drawedBitmap, blgRecS, blgRecD, paint);
        Paint rectp = new Paint();
        rectp.setColor(Color.YELLOW);
        rectp.setAlpha((int)(50));
        canvas.drawRect(0, 0, W/4, H/4, rectp);
        canvas.drawRect(3*W/4, 3*H/4, W, H, rectp);
        canvas.drawRect(0, 3*H/4, W/4, H, rectp);
        canvas.drawRect(3*W/4, 0, W, H/4, rectp);
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
                    startL2RAnimation();
                if(touchinx >= 0 && touchinx < W/4 && touchiny > 3*H/4 ) //down-left
                    startFallAnimation();
                if(touchinx > 3*W/4 && touchiny < H/4 && touchiny >= 0 ) //up-right
                    startSizeAnimation();
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
                else if(pet_state != PET_STATE.ONBOOM && pet_state != PET_STATE.L2R &&
                        pet_state!=PET_STATE.R2L && pet_state!=PET_STATE.PRIVATE)
                    pet_state = PET_STATE.NORMAL;

                break;
        }
        return true;
    }
    //anim
    public void startL2RAnimation() {
        Log.i("anim", "herre");
        setUntouchable(true);
        pet_state = PET_STATE.L2R;
        float fromx = x, tox = x + W/3;
        if(tox > screenW-W) tox = screenW-W;
        Random r = new Random();
        int step = r.nextInt(10);
        step = 5-step;
        float fromy = y, toy = y + (step*screenH/10);
        if(toy < 0) toy = 0;
        if(toy > screenH-H) toy = screenH-H;
        distance = screenW - W;
        int dura = (int)((distance-x)/distance * 3000);
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
                pet_state = PET_STATE.NORMAL;
                setUntouchable(false);
            }
        });
        animPosi.setDuration(dura);
        animPosi.setRepeatCount(Animation.ABSOLUTE);
        animPosi.setInterpolator(new LinearInterpolator());//设置插值器
        animPosi.start();
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
        distance = screenW - W;
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
                setUntouchable(false);
            }
        });
        animPosi.setDuration(dura);
        animPosi.setRepeatCount(Animation.ABSOLUTE);
        animPosi.setInterpolator(new LinearInterpolator());//设置插值器
        animPosi.start();
    }
    public void startFallAnimation() {
        setUntouchable(true);
        pet_state = PET_STATE.PRIVATE;
        float fromx = x, tox = x - W/6;
        if(tox < 0) tox = 0;
        float fromy = y, toy = y;
        distance = x-tox;
        fallStartX = fromx;
        int dura = (int)(1000);
        PetPosiValue startVal = new PetPosiValue(fromx, fromy);
        PetPosiValue endVal = new PetPosiValue(tox, toy);
        animPosi = ValueAnimator.ofObject(new PetPosiEvaluator(), startVal, endVal);
        animPosi.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentPetFallVal = (PetPosiValue) animation.getAnimatedValue();
                invalidate();
            }
        });
        animPosi.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startR2LAnimation();
//                pet_state = PET_STATE.NORMAL;
                setUntouchable(false);
                invalidate();
            }
        });
        animPosi.setDuration(dura);
        animPosi.setRepeatCount(Animation.ABSOLUTE);
        animPosi.setInterpolator(new LinearInterpolator());//设置插值器
        animPosi.setInterpolator(new AccelerateInterpolator());
        animPosi.start();
    }
}
