package com.hudson.wanandroid.ui.view.indicatorviewpager.indicator;

/**
 * ViewPager's indicator.
 * Override {@link #onScrollPage(int, float)} to handle page changing event,
 * you can make many different types of indicators.
 * Created by Hudson on 2019/1/31.
 */
public interface IPagerIndicator {
    /**
     * If ViewPager is changing pages,this method will be invoked.
     * Maybe you should call {@link android.view.View#invalidate} to refresh indicators.
     * @param position current ViewPager's first visible index. {position + 1} is target page's index
     * @param fraction the scrolling fraction which is range from 0 to 1.
     */
    void onScrollPage(int position, float fraction);

    void setCount(int pageCount);
}
