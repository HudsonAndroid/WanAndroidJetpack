package com.hudson.wanandroid.ui.view.indicatorviewpager.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hudson.wanandroid.ui.view.indicatorviewpager.indicator.IPagerIndicator;
import com.hudson.wanandroid.ui.view.indicatorviewpager.listener.AnimateOnPageChangeListener;

/**
 * In this view you can associate with some indicators which change
 * position with ViewPager changing,call method
 * {@link #attachIndicator(IPagerIndicator...)} to attach indicators.
 * Created by Hudson on 2019/5/24.
 */
public class CanIndicateViewPager extends ViewPager {
    public CanIndicateViewPager(Context context) {
        this(context, null);
    }

    public CanIndicateViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Attach indicators to ViewPager. You should make sure the adapter
     * has been initialized before you do it.
     * @param indicators indicator array
     */
    public void attachIndicator(@NonNull IPagerIndicator... indicators){
        PagerAdapter adapter = getAdapter();
        if(adapter == null){
            Log.w("CanIndicateViewPager","ViewPager doesn't have any PagerAdapter,so indicators attach failed !");
            return ;
        }
        for (IPagerIndicator pagerIndicator : indicators) {
            addOnPageChangeListener(new AnimateOnPageChangeListener(pagerIndicator,this));
        }
    }

    public void destroy(){
        clearOnPageChangeListeners();
    }
}
