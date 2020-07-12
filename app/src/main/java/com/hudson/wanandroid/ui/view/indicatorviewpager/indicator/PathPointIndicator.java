package com.hudson.wanandroid.ui.view.indicatorviewpager.indicator;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 实心圆左右切换时附带水珠效果过渡带
 * Created by Hudson on 2019/1/31.
 */
public class PathPointIndicator extends CirclePathPointIndicator {
    public PathPointIndicator(Context context) {
        this(context, null);
    }

    public PathPointIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathPointIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected float getControlX(float leftX, float rightX, float fraction) {
        return leftX + mCircleMaxRadius + (rightX - leftX - mCircleMaxRadius*2)*fraction;
    }

    @Override
    protected float getControlY(float leftY, float rightY, float fraction) {
        return getPaddingTop() + mCircleMaxRadius;
    }
}
