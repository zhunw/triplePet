package com.example.a91927.triplepet.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.example.a91927.triplepet.R;
import com.example.a91927.triplepet.util.PetAlphaEvaluator;
import com.example.a91927.triplepet.util.PetAlphaValue;
import com.example.a91927.triplepet.util.PetPosiValue;
import com.example.a91927.triplepet.util.PetSizeEvaluator;
import com.example.a91927.triplepet.util.PetSizeValue;

import java.util.List;

import static java.lang.Math.min;

public class BasePetView extends View {
    /**画笔*/
    protected Paint paint;
    /**界面大小，宽高*/
    protected int screenW, screenH;
    /**View的宽高*/
    protected int W, H;
    /**当前系统标题栏的高度，如果全屏则为0*/
    protected float titleBarH;
    /**资源类*/
    protected Resources res;
    /**移动速度, 默认为5*/
    protected int speed = 5;
    /**当前坐标位置*/
    protected float x, y;
    /**触摸时的坐标*/
    protected float touchX, touchY;
    protected float touchinx, touchiny;
    /**触摸状态(长按用),0开启未触摸；1开启且触摸*/
    protected boolean onPressing = false;
    /**触摸down的时间*/
    protected long touchDownTime;
    /**公共，简化*/
    protected Matrix matrix;
    /* ************** zhunw add *********** */

    /* 被画的bitmap */
    protected  Bitmap drawedBitmap;
    protected  Bitmap hide_left;
    protected  Bitmap hide_right;
    protected  Bitmap bmp_tmp;

    public enum PET_STATE {NORMAL, HIDE_LEFT, HIDE_RIGHT, ONBOOM, L2R, R2L, PRIVATE};
    public PET_STATE pet_state = PET_STATE.NORMAL;

    long diffTime;
    boolean untouchable = false;
    int init_height, init_width;
    final float boom_rate = 1.5f;

    float tmpx, tmpy;

    protected int numOfBmp;
    protected int idx = 0;
    protected Bitmap[] bmpArray;
    //动画抽象距离，用于控制时间
    protected float distance;
    public int delayTime = 500;

    /* ************* zhunw end ************* */
    /**  重构的成员 **/
    protected ValueAnimator animAlpha = new ValueAnimator();
    protected ValueAnimator animSize = new ValueAnimator();
    protected ValueAnimator animPosi = new ValueAnimator();
    protected PetAlphaValue currentPetAlphaVal = new PetAlphaValue(1.0f);
    protected PetSizeValue currentPetSizeVal ;
    protected PetPosiValue currentPetPosiVal = new PetPosiValue(0f, 0f);

    //获取人物大小配置文件
//    protected SharedPreferences sharedPreferences;
    public BasePetView(Context context) {
        super(context);
    }
    public BasePetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BasePetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    /**** public 需继承的*******************************************************/
//    public void initial(SharedPreferences sp) {
//    }
    protected void initBmp() {
        return;
    }
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
    /********************************************************************/
    /**测量屏幕高宽,由于会出现屏幕旋转问题，所以需要改变*/
    public void measureScreen(){
        screenH = res.getDisplayMetrics().heightPixels;
        screenW = res.getDisplayMetrics().widthPixels;
        init_height = (int)(screenH * 0.2);
        init_width = init_height;
    }
    protected void initValues() {
        currentPetSizeVal = new PetSizeValue(init_width, init_height);
        W = (int)currentPetSizeVal.W;
        H = (int)currentPetSizeVal.H;
        x = 300;
        y = 500;
    }
    /** 获得坐标x */
    public float getX(){
        return x;
    }
    /** 获得坐标y */
    public float getY(){
        return y;
    }
    /** 获得当前图片的宽度 */
    public int getW(){
        return (int)W;
    }
    /** 获得当前图片的高度 */
    public int getH(){
        return (int)H;
    }
    public long getTouchDownTime() {
        return touchDownTime;
    }

    protected Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }
    protected void canvasDraw(Canvas canvas, Bitmap bitmap, Matrix matrix, Paint paint){
        if(bitmap != null){
            canvas.drawBitmap(bitmap, matrix, paint);
        }
    }

    /*  zhunw method */
    public void setOnPressing(boolean onPerson) {
        this.onPressing = onPerson;
    }
    public boolean getOnPressing() {
        return onPressing;
    }
    public long getDiffTime() {
        return diffTime;
    }
    public int  getRawID(String Name) {
        int resId = 0;
        try {
            resId = R.raw.class.getField(Name).getInt(null);
        }
        catch (Exception exp) {
            Log.i("exp", exp.getMessage());
        }
        return resId;
    }
    public int  getDrawableID(String Name) {
        int resId = 0;
        try {
            resId = R.drawable.class.getField(Name).getInt(null);
        }
        catch (Exception exp) {
            Log.i("exp", exp.getMessage());
        }
        return resId;
    }
    public void setIdx(int i) {
        idx = i;
    }
    public int getIdx() {
        return idx;
    }
    public int getNumOfBmp()  {
        return numOfBmp;
    }
    public void setUntouchable(boolean b) {
        untouchable = b;
    }

    //重构的函数
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
//        if (tmpw > screenW) tmpw = screenW;
//        if (tmph > screenH) tmph = screenH;
//        float tmps = min(tmph, tmpw);
//        tmph = tmpw = tmps;
        PetSizeValue startVal = new PetSizeValue(init_width, init_height);
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
}
