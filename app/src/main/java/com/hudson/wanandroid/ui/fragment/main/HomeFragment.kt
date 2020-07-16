package com.hudson.wanandroid.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.HomeRepository
import com.hudson.wanandroid.databinding.FragmentHomeBinding
import com.hudson.wanandroid.ui.adapter.BannerAdapter
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.view.indicatorviewpager.listener.SimplePageChangeListener
import com.hudson.wanandroid.viewmodel.BannerModel

/**
 * Created by Hudson on 2020/7/12.
 */
class HomeFragment: Fragment() {

    private lateinit var homeBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bannerModel = BannerModel(
            HomeRepository(
                WanAndroidApi.api(),
                WanAndroidDb.getInstance(context!!).dataWrapperDao()
            )
        )
        homeBinding.banner = bannerModel
        homeBinding.adapter = BannerAdapter()
        homeBinding.bannerCount = bannerModel.bannerCount
        homeBinding.bannerTitle = bannerModel.bannerTitle
        homeBinding.indicator = homeBinding.ppIndicator // can we simply?
        homeBinding.pageChangeListener = object : SimplePageChangeListener(){
            override fun onPageSelected(position: Int) {
                bannerModel.onPageChanged(position)
            }
        }
        homeBinding.retry = object : RetryCallback {
            override fun retry() {
                bannerModel.retry()
            }
        }
        homeBinding.banners = bannerModel.bannersLiveData
        homeBinding.lifecycleOwner = this //自动为LiveData设置数据观察绑定，相当于liveData.observe自动完成

        bannerModel.bannersLiveData.observe(this,
            Observer<Resource<List<BannerItem>>> {
                homeBinding.adapter!!.refresh(homeBinding.vpBanner,it.data)
                bannerModel.refresh()
                homeBinding.vpBanner.startAutoSwitch()
                Log.d(javaClass.simpleName,"${it.status}, ${it.data}, ${it.msg}")
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        homeBinding.vpBanner.clearOnPageChangeListeners()
    }
}