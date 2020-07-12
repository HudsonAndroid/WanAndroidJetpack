package com.hudson.wanandroid.ui.view.indicatorviewpager.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.hudson.wanandroid.R;


/**
 * Indicator which is from small circle to big circle.
 * 默认情况下，内容绘制在控件上方（水平方向上充分利用）
 * Created by Hudson on 2019/1/31.
 */
public class CirclePointIndicator extends View implements IPagerIndicator{
    private static final float SPACE_RATE = 0.1f;//空隙占据
    private static final float CIRCLE_RATE = 0.55f;//普通圆相比最大圆的占比
    protected float mCircleRate;
    protected int mCount;
    protected float mSpaceDimen;
    protected float mCircleRadius;
    protected float mCircleMaxRadius;
    protected float mCircleOffset;
    protected Paint mPaint;
    protected int mPosition;
    protected float mFraction;

    public CirclePointIndicator(Context context) {
        this(context, null);
    }

    public CirclePointIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePointIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(context.getResources().getColor(R.color.colorAccent));
        mCircleRate = CIRCLE_RATE;
    }

    public void setCount(int count) {
        mCount = count;
        calculateDimen();
    }

    protected void calculateDimen(){
        if(mCount <= 0 || getWidth() <= 0 || getHeight() <= 0){
            return ;
        }
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        mSpaceDimen = width * SPACE_RATE;
        mCircleMaxRadius = (width - (mCount - 1)*mSpaceDimen) / mCount / 2;
        float offset = mCircleMaxRadius - height/2;
        if(offset > 0){//由宽度确定的情况下，高度不够
            mSpaceDimen += offset*2;
            mCircleMaxRadius = height/2;
        }
        mCircleRadius = mCircleMaxRadius*mCircleRate;
        mCircleOffset = mCircleMaxRadius*(1-mCircleRate);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimen();
    }

    @Override
    public void onScrollPage(int position, float fraction) {
        mPosition = position;
        mFraction = fraction;
        prepareBeforeUpdate(position,fraction);
        invalidate();
    }

    protected void prepareBeforeUpdate(int position,float fraction){}

    @Override
    protected void onDraw(Canvas canvas) {
        float startX = getPaddingLeft() + mCircleMaxRadius;
        float y = getPaddingTop() + mCircleMaxRadius;
        float offset = mCircleOffset * mFraction;
        for (int i = 0; i < mCount; i++) {
            if(mPosition == i){
                canvas.drawCircle(startX,y,mCircleMaxRadius - offset,mPaint);
            }else if(mPosition == i-1){
                canvas.drawCircle(startX,y,mCircleRadius + offset,mPaint);
            }else{
                canvas.drawCircle(startX,y,mCircleRadius,mPaint);
            }
            startX += mCircleMaxRadius*2 + mSpaceDimen;
        }
    }
}
