package com.hudson.wanandroid.ui.view.indicatorviewpager.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Indicator which is from empty circle to full circle.
 * Created by Hudson on 2019/2/1.
 */
public class CircleFillIndicator extends CirclePointIndicator {
    private static final float EMPTY_STROKE_WIDTH = 2;//px

    public CircleFillIndicator(Context context) {
        this(context, null);
    }

    public CircleFillIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleFillIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void calculateDimen() {
        super.calculateDimen();
        //绘制时圆的半径占据1/2，显示效果是全部的,由于stroke是向内外拓展，这个问题最好结合图分析
        //此时，mCircleMaxRadius是fill时的stroke，EMPTY_STROKE_WIDTH是默认的stroke
        mCircleRadius = mCircleMaxRadius/2;
        mCircleOffset = mCircleMaxRadius - EMPTY_STROKE_WIDTH;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float startX = getPaddingLeft() + mCircleMaxRadius;
        float y = getPaddingTop() + mCircleMaxRadius;
        float offset = mCircleOffset * mFraction;
        for (int i = 0; i < mCount; i++) {
            if(mPosition == i){
                mPaint.setStrokeWidth(mCircleMaxRadius - offset);
            }else if(mPosition == i-1){
                mPaint.setStrokeWidth(EMPTY_STROKE_WIDTH + offset);
            }else{
                mPaint.setStrokeWidth(EMPTY_STROKE_WIDTH);
            }
            canvas.drawCircle(startX,y,mCircleRadius,mPaint);
            startX += mCircleMaxRadius*2 + mSpaceDimen;
        }
    }
}
