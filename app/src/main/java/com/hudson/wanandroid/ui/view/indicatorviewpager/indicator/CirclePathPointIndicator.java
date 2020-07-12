package com.hudson.wanandroid.ui.view.indicatorviewpager.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;

/**
 * 实心圆左右切换时附带锥形的过渡带
 * Created by Hudson on 2019/1/31.
 */
public class CirclePathPointIndicator extends CirclePointIndicator {
    private Path mPath;

    public CirclePathPointIndicator(Context context) {
        this(context, null);
    }

    public CirclePathPointIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePathPointIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPath = new Path();
    }

    @Override
    protected void prepareBeforeUpdate(int position, float fraction) {
        mPath.reset();
        if(fraction != 0 && fraction != 1.0f){
            float leftTopX = getPaddingLeft() + mCircleMaxRadius + (mCircleMaxRadius*2+mSpaceDimen)*position;
            float changeValue = mCircleOffset * fraction;
            float leftTopY = getPaddingTop() + changeValue;
            float rightTopX = leftTopX + (mCircleMaxRadius*2+mSpaceDimen);
            float rightChange = mCircleOffset * (1 - fraction);
            float rightTopY = getPaddingTop() + rightChange;
            float leftBottomY = leftTopY + (mCircleMaxRadius - changeValue)*2;
            float rightBottomY = rightTopY + (mCircleMaxRadius - rightChange)*2;
            mPath.moveTo(leftTopX,leftTopY);
            float controlX = getControlX(leftTopX,rightTopX,fraction);
            mPath.quadTo(controlX,getControlY(leftTopY,rightTopY,fraction),rightTopX,rightTopY);
            mPath.lineTo(rightTopX,rightBottomY);
            mPath.quadTo(controlX,getControlY(leftBottomY,rightBottomY,fraction),leftTopX,leftBottomY);
            mPath.close();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }

    protected float getControlY(float leftY,float rightY,float fraction){
        return (leftY + rightY)/2;
    }

    protected  float getControlX(float leftX,float rightX,float fraction){
        return (leftX + rightX)/2;
    }
}
