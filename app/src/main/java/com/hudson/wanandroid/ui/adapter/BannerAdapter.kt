package com.hudson.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.databinding.ItemBannerImgBinding

/**
 * Created by Hudson on 2020/7/11.
 */
class BannerAdapter: PagerAdapter() {
    private val views: MutableList<View> = mutableListOf()

    fun refresh(parent: ViewGroup, banners: List<BannerItem>?){
        if(banners != null){
            views.clear()
            for(bannerItem in banners){
                val binding =
                    ItemBannerImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                views.add(binding.root)
                binding.bannerItem = bannerItem
            }
            notifyDataSetChanged()
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = views[position]
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(views[position])
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return views.size
    }
}