package com.example.a91927.triplepet.view;

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

import com.example.a91927.triplepet.R;

import java.util.List;

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

    protected enum PET_STATE {NORMAL, HIDE_LEFT, HIDE_RIGHT, ONBOOM};
    protected  PET_STATE pet_state = PET_STATE.NORMAL;

    long diffTime;
    boolean touchAnimAlpha = false;
    boolean touchAnimSize = false;
    boolean untouchable = false;
    final int init_size = 300;
    final float boom_rate = 1.5f;

    protected int numOfBmp;
    protected int idx = 0;
    protected Bitmap[] bmpArray;

    /* ************* zhunw end ************* */
    /**用户选择的动画数组，随机出现*/
    protected List<Integer> actionGroup;
    /***************标识,常量************************/
    /**是否绘制时间,是否能绘制*/
    protected boolean drawTime;
    /**当前是否绘制时间,前提是drawTime=true*/
    protected boolean drawTimeNow;
    /**绘制时间标识数*/
    protected int drawTimeFlag;
    /**动画标识*/
    protected int actionFlag;
    /**单个动画变化标识*/
    protected int frameFlag;
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
    public void startAlphaAnimation() {return; }
    public void startSizeAnimation() {return; }
    public void stopAnimation() { return;}
    public void stopSizeAnimation() { return;}
    /********************************************************************/
    /**测量屏幕高宽,由于会出现屏幕旋转问题，所以需要改变*/
    public void measureScreen(){
        screenH = res.getDisplayMetrics().heightPixels;
        screenW = res.getDisplayMetrics().widthPixels;
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
    public boolean getTouchAnimAlpha() {
        return touchAnimAlpha;
    }
    public boolean getTouchAnimSize() {
        return touchAnimSize;
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
}
