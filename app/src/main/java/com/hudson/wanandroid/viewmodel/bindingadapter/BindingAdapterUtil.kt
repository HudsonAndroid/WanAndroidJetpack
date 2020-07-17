package com.hudson.wanandroid.viewmodel.bindingadapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.hudson.wanandroid.ui.view.indicatorviewpager.indicator.CirclePointIndicator
import com.hudson.wanandroid.ui.view.indicatorviewpager.indicator.IPagerIndicator
import com.hudson.wanandroid.ui.view.indicatorviewpager.listener.SimplePageChangeListener
import com.hudson.wanandroid.ui.view.indicatorviewpager.viewpager.AutoSwitchViewPager

/**
 * 注意BindingAdapter第一个参数类型必须属于view类型
 * Created by Hudson on 2020/7/11.
 */
@BindingAdapter(value = ["bannerAdapter"])
fun bannerAdapter(autoSwitchViewPager: AutoSwitchViewPager, pagerAdapter: PagerAdapter){
    autoSwitchViewPager.adapter = pagerAdapter
}

//DataBinding可以选择性在处理时使用旧值
@BindingAdapter(value = ["bannerChangeListener"])
fun bannerChangeListener(autoSwitchViewPager: AutoSwitchViewPager, oldListener: SimplePageChangeListener?,
                         newListener: SimplePageChangeListener?){
    if(oldListener != null){
        autoSwitchViewPager.removeOnPageChangeListener(oldListener)//移除旧监听器
    }
    if(newListener != null){
        autoSwitchViewPager.addOnPageChangeListener(newListener)
    }
}

@BindingAdapter(value = ["imageUrl","errorDrawable"], requireAll = false)
fun imageUrl(imageView: ImageView, url:String?, error:Drawable?){
    Glide.with(imageView.context).load(url).error(error).into(imageView)
}

//指示器圆点个数
@BindingAdapter(value = ["indicatorCount"])
fun indicatorCount(indicator: CirclePointIndicator, bannerCount: Int){
    indicator.setCount(bannerCount)
}

//首页banner指示器
@BindingAdapter(value = ["indicator"])
fun indicator(autoSwitchViewPager: AutoSwitchViewPager, indicator: IPagerIndicator){
    autoSwitchViewPager.attachIndicator(indicator)
}

@BindingAdapter(value = ["showOrHide"])
fun showOrHide(view: View, show: Boolean){
    view.visibility =  if(show) View.VISIBLE else View.GONE
}