package com.example.a91927.triplepet.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
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
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.example.a91927.triplepet.R;
import com.example.a91927.triplepet.util.PetAlphaEvaluator;
import com.example.a91927.triplepet.util.PetAlphaValue;
import com.example.a91927.triplepet.util.PetPosiEvaluator;
import com.example.a91927.triplepet.util.PetPosiValue;
import com.example.a91927.triplepet.util.PetSizeEvaluator;
import com.example.a91927.triplepet.util.PetSizeValue;

import java.util.Random;

import static java.lang.Math.min;
import static java.lang.Thread.sleep;

public class PikachuView extends BasePetView {
    ValueAnimator animAlpha = new ValueAnimator();
    ValueAnimator animSize = new ValueAnimator();
    ValueAnimator animPosi = new ValueAnimator();
    PetAlphaValue currentPetAlphaVal = new PetAlphaValue(1.0f);
    PetSizeValue currentPetSizeVal = new PetSizeValue(init_size, init_size);
    PetPosiValue currentPetPosiVal = new PetPosiValue(0f, 0f);
    Bitmap[] bmpL2RAnimArray;
    final int numOfToRightAnim = 5;
    Bitmap[] bmpR2LAnimArray;
    final int numOfToLeftAnim = 5;

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
        setFocusable(true);
        res = context.getResources();
        measureScreen();
        Log.i("log", String.format("w:%d, h:%d", screenW, screenH));
        initBmp();
        initPosiBmp();
        initToLeftBmp();
        //test
        bmp_tmp = BitmapFactory.decodeResource(res, R.drawable.pika_largest);
//        pet_state = PET_STATE.NORMAL;
//        pet_state = PET_STATE.IN_ANIM_POSI;
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
    private void initPosiBmp() {
        bmpL2RAnimArray = new Bitmap[numOfToRightAnim];
        String str = "walk_to_right";
        for(int i = 1; i <= numOfBmp; i++) {
            String name = str + Integer.toString(i);// + ".png";
            bmpL2RAnimArray[i-1] = decodeResource(res, getDrawableID(name));
        }
    }
    private void initToLeftBmp() {
        bmpR2LAnimArray = new Bitmap[numOfToLeftAnim];
        String str = "walk_to_left";
        for(int i = 1; i <= numOfBmp; i++) {
            String name = str + Integer.toString(i);// + ".png";
            bmpR2LAnimArray[i-1] = decodeResource(res, getDrawableID(name));
        }
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
                int tmpidx = (int)(x/single);
                tmpidx = tmpidx % numOfToRightAnim;
                drawedBitmap = bmpL2RAnimArray[tmpidx];
                break;
            case R2L:
                x = currentPetPosiVal.x;
                y = currentPetPosiVal.y;
                float single_ = (screenW-W)/(float)numOfToLeftAnim/3; //每个区间长度
                int tmpidx_ = (int)(x/single_);
                tmpidx = tmpidx_ % numOfToLeftAnim;
                drawedBitmap = bmpR2LAnimArray[tmpidx];
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
//        rectp.set
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
        //for special ainm
//        touchinx = -1;
//        touchiny = -1;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(touchinx >= 0 && touchinx < W/4 && touchiny < H/4 && touchiny >= 0 ) //up-left
                    startAlphaAnimation();
                if(touchinx > 3*W/4 && touchiny > 3*H/4 ) //down-right
                    startL2RAnimation();
                if(touchinx >= 0 && touchinx < W/4 && touchiny > 3*H/4 ) //down-left
                    startR2LAnimation();
                if(touchinx > 3*W/4 && touchiny < H/4 && touchiny >= 0 ) //up-right
                    startSizeAnimation();
//                Log.i("log", String.format("touch %d %d", touchinx, touchiny));
                if(pet_state == PET_STATE.ONBOOM) {
                    currentPetSizeVal.W = init_size;
                    currentPetSizeVal.H = init_size;
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
                else if(pet_state != PET_STATE.ONBOOM && pet_state != PET_STATE.L2R && pet_state!=PET_STATE.R2L)
                    pet_state = PET_STATE.NORMAL;

                break;
        }
        return true;
    }
    //anim
    public void startAlphaAnimation() {
        setUntouchable(true);
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
        animAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                currentPetAlphaVal.alpha = 1.0f;
                setUntouchable(false);
            }
        });
        animAlpha.setDuration(2000);
        animAlpha.setRepeatCount(Animation.ABSOLUTE);
        animAlpha.setInterpolator(new LinearInterpolator());//设置插值器
        animAlpha.start();
    }
    public void startSizeAnimation() {
        setUntouchable(true);
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
        animSize.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                pet_state = PET_STATE.ONBOOM;
                setUntouchable(false);
            }
        });
        animSize.setDuration(2000);
        animSize.setRepeatCount(Animation.ABSOLUTE);
        animSize.setInterpolator(new LinearInterpolator());//设置插值器
        animSize.start();

    }
    public void startL2RAnimation() {
        Log.i("anim", "herre");
        setUntouchable(true);
        pet_state = PET_STATE.L2R;
        float fromx = x, tox = screenW-W;
        Random r = new Random();
        int step = r.nextInt(10);
        step = 5-step;
        float fromy = y, toy = y + (step*screenH/10);
        if(toy < 0) toy = 0;
        if(toy > screenH-H) toy = screenH-H;
        float distance = screenW - W;
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
                pet_state = PET_STATE.HIDE_RIGHT;
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
        Log.i("log", "tox"+Float.toString(tox));
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
                setUntouchable(false);
            }
        });
        animPosi.setDuration(dura);
        animPosi.setRepeatCount(Animation.ABSOLUTE);
        animPosi.setInterpolator(new LinearInterpolator());//设置插值器
        animPosi.start();
    }
}
