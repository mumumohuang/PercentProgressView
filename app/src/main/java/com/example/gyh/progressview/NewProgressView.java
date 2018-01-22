package com.example.gyh.progressview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by youban01 on 2018/1/15.
 */

public class NewProgressView extends View {

    private Paint mBorderPaint;//边框画笔
    private Paint mFillPaint; //填充画笔

    private int mHeight;//view获得的总高度
    private int mWidth;//view获得的总宽度
    private int mBorderDrawHeight;//外边框应该画的高度 以及两边边框圆弧的直径
    //上下左右的padding值
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mPaddingRight;

    private float mBorderWidth = 2f; //外边框的宽度
    private int mProgress = 0;//进度条
    private int mViewWidth;
    private float realProgress;//根据Progress和width计算出的实际进度值
    private Paint mTextPaint;
    private float mid; //中间矩形的长度

    public NewProgressView(Context context) {
        super(context);
        initView();
    }

    public NewProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NewProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    private void initView() {
        mBorderPaint = new Paint();
        mBorderPaint.setColor(0xffff0000);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mFillPaint = new Paint();
        mFillPaint.setColor(0xff16ffff);
        mFillPaint.setStrokeWidth(mBorderWidth);
        mFillPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(0xff000000);
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(50f);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingBottom = getPaddingBottom();
        mPaddingRight = getPaddingRight();
        mBorderDrawHeight = mHeight - mPaddingTop - mPaddingBottom;
        mViewWidth = mWidth - mPaddingLeft - mPaddingRight;
        //中间部分的长度
        mid = mViewWidth - Float.valueOf(mBorderDrawHeight);
    }

    public void setProgress(int progress) {
        if (progress < 0){
            mProgress = 0;
        }else if (progress > maxValue){
            mProgress = (int) maxValue;
        }else {
            mProgress = progress;
        }
        //根据总长度获取实时进度值
//        realProgress = Float.valueOf(mProgress) * ((Float.valueOf(mViewWidth)) / 100);
        invalidate();
    }
    private float maxValue = 100;
    public void setMaxValue(int maxValue){
        this.maxValue = maxValue;
    }
    public void setBorderColor(int color){
        mBorderPaint.setColor(color);
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        RectF rectLeft = new RectF(mPaddingLeft, mPaddingTop, mBorderDrawHeight + mPaddingLeft, mBorderDrawHeight + mPaddingTop);
        canvas.drawArc(rectLeft, 90, 180, false, mBorderPaint);
        canvas.drawLine(mPaddingLeft + mBorderDrawHeight / 2, mPaddingTop, mWidth - mPaddingRight - mBorderDrawHeight / 2, mPaddingTop, mBorderPaint);
        canvas.drawLine(mPaddingLeft + mBorderDrawHeight / 2, mBorderDrawHeight + mPaddingTop, mWidth - mPaddingRight - mBorderDrawHeight / 2, mBorderDrawHeight + mPaddingTop, mBorderPaint);
        RectF rectRight = new RectF(mWidth - mBorderDrawHeight - mPaddingRight, mPaddingTop, mWidth - mPaddingRight, mBorderDrawHeight + mPaddingTop);
        canvas.drawArc(rectRight, 270, 180, false, mBorderPaint);
        realProgress = mProgress * (Float.valueOf(mViewWidth) / maxValue);

        //根据长度计算角度  半径减去前面的
        float radius = Float.valueOf(mBorderDrawHeight) / 2;  //半径
        float dx = radius - realProgress;   //半径减去进度值
        if (dx < 0) dx = 0;
        float dy = radius * radius - dx * dx;
        double a = Math.sqrt(dy) / radius;
        float asin = (float) Math.toDegrees(Math.acos(a));
        //前面的半圆
        RectF rectLeftFill = new RectF(mPaddingLeft + mBorderWidth, mPaddingTop + mBorderWidth, mBorderDrawHeight + mPaddingLeft, mBorderDrawHeight - mBorderWidth + mPaddingTop);
        canvas.drawArc(rectLeftFill, 90 + asin, 180 - asin * 2, false, mFillPaint);

        if (realProgress > radius) { //超过半圆了 画矩形
            //中间的矩形
            float temp = realProgress + mPaddingLeft;
            //进入了最后一个半圆的区域
            if (realProgress > mViewWidth - radius) {
                temp = mViewWidth - radius + mPaddingLeft;
            }
            RectF midRectf = new RectF(mPaddingLeft + radius, mPaddingTop + mBorderWidth, temp, mBorderDrawHeight + mPaddingTop - mBorderWidth);
            canvas.drawRect(midRectf, mFillPaint);
        }

        //进入了最后一个半圆的区域
        //后面的半圆  画法有点特殊 准确的说是一个梯弧形(两个扇形加两个三角形)
        if (realProgress > mViewWidth - radius) {
            float x = realProgress - mid - radius;
            float degress = (float) Math.toDegrees(Math.asin(x / radius));
            RectF rectRightFill = new RectF(mViewWidth - radius * 2 + mPaddingLeft, mPaddingTop + mBorderWidth, mWidth - mPaddingRight, mBorderDrawHeight - mBorderWidth + mPaddingTop);
            canvas.drawArc(rectRightFill, 270, degress, true, mFillPaint);
            canvas.drawArc(rectRightFill, 90 - degress, degress, true, mFillPaint);
            Path path = new Path();
            path.moveTo(getWidth() - radius -mPaddingRight, mHeight / 2);
            path.lineTo(getWidth() - radius -mPaddingRight+x, (float) (mHeight / 2 - Math.sqrt(radius*radius - x*x)));
            path.lineTo(getWidth() - radius -mPaddingRight+x,(float) (mHeight / 2 + Math.sqrt(radius*radius - x*x)));
            path.close();
            canvas.drawPath(path,mFillPaint);
        }

        float textWidth = mTextPaint.measureText((int)Math.floor(realProgress/mViewWidth*100) + "%");
        //文字的y轴坐标
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float y = mHeight / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;
        canvas.drawText((int)Math.floor(realProgress/mViewWidth*100) + "%", Float.valueOf(mWidth) / 2 - textWidth / 2, y, mTextPaint);

    }
}
