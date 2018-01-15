package com.example.gyh.progressview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by youban01 on 2018/1/15.
 */

public class ProgressView extends View {

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
    private float realProgress;
    private Paint mTextPaint;

    public ProgressView(Context context) {
        super(context);
        initView();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
    }

    public void setProgress(int progress) {
        mProgress = progress;
        //根据总长度获取实时进度值
        realProgress = Float.valueOf(mProgress) * ((Float.valueOf(mViewWidth) - Float.valueOf(mBorderDrawHeight)) / 100);
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

        //前面的半圆
        RectF rectLeftFill = new RectF(mPaddingLeft + mBorderWidth, mPaddingTop + mBorderWidth, mBorderDrawHeight + mPaddingLeft, mBorderDrawHeight - mBorderWidth + mPaddingTop);
        canvas.drawArc(rectLeftFill, 90, 180, true, mFillPaint);

        //中间的矩形
        RectF midRectf = new RectF(mPaddingLeft + mBorderDrawHeight / 2, mPaddingTop + mBorderWidth, realProgress + mPaddingLeft + mBorderDrawHeight / 2, mBorderDrawHeight + mPaddingTop - mBorderWidth);
        canvas.drawRect(midRectf, mFillPaint);

        //后面的半圆
        RectF rectRightFill = new RectF(mPaddingLeft + realProgress, mPaddingTop + mBorderWidth, realProgress + mPaddingLeft + mBorderDrawHeight - mBorderWidth, mBorderDrawHeight - mBorderWidth + mPaddingTop);
        canvas.drawArc(rectRightFill, 270, 180, true, mFillPaint);



        float textWidth = mTextPaint.measureText(mProgress + "%");

        //文字的y轴坐标
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float y = mHeight / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2;

        canvas.drawText(mProgress + "%", mWidth / 2 - textWidth / 2, y, mTextPaint);
    }
}
