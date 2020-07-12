package com.hudson.wanandroid.ui.view.indicatorviewpager.listener;

import androidx.viewpager.widget.ViewPager;

import com.hudson.wanandroid.ui.view.indicatorviewpager.indicator.IPagerIndicator;

import java.lang.ref.WeakReference;

/**
 * Created by Hudson on 2019/1/31.
 */
public class AnimateOnPageChangeListener implements ViewPager.OnPageChangeListener {
    private final WeakReference<IPagerIndicator> mAnimateRef;
    private ViewPager mViewPager;

    public AnimateOnPageChangeListener(IPagerIndicator indicator, ViewPager viewPager) {
        //使用弱引用，避免ViewPager对IPagerIndicator的间接依赖
        mAnimateRef = new WeakReference<>(indicator);
        mViewPager = viewPager;
        //刷新初始位置
        onPageChanging(viewPager.getCurrentItem(),0);
    }

    @Override
    public void onPageScrollStateChanged(int state) { }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        onPageChanging(position,positionOffsetPixels*1.0f/mViewPager.getWidth());
    }

    @Override
    public void onPageSelected(int position) {
        onPageChanging(position,0f);
    }

    private void onPageChanging(int position,float fraction){
        final IPagerIndicator animateInstance = mAnimateRef.get();
        if(animateInstance != null) {
            animateInstance.onScrollPage(position,fraction);
        }
    }
}
