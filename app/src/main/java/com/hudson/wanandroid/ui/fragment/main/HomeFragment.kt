package com.hudson.wanandroid.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.databinding.FragmentHomeBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.BannerAdapter
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.ui.view.indicatorviewpager.listener.SimplePageChangeListener
import com.hudson.wanandroid.viewmodel.BannerModel
import javax.inject.Inject

/**
 * 注入代码在AppInjector中完成
 * Created by Hudson on 2020/7/12.
 */
class HomeFragment: Fragment() , Injectable{
    private var homeBinding by autoCleared<FragmentHomeBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // 以下来源自fragment-ktx kotlin扩展库，用于创建ViewModel，
    // 相当于ViewModelProvider(fragment, factory).get(BannerModel::class.java)
    val bannerModel: BannerModel by viewModels {
        viewModelFactory
    }

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
        //注释的内容将通过Dagger注入得到ViewModelProvider.Factory，然后根据它创建BannerModel
        //因此外界并不知道BannerModel如何实现的，BannerModel如果要修改，不会影响到HomeFragment
//        //应该使用ViewModelProvider来构建
//        val bannerModel = BannerModel(
//            HomeRepository(
//                WanAndroidApi.api(),
//                WanAndroidDb.getInstance(context!!).dataWrapperDao()
//            )
//        )
        homeBinding.banner = bannerModel
        homeBinding.adapter = BannerAdapter()
        homeBinding.bannerCount = bannerModel.bannerCount
        homeBinding.bannerTitle = bannerModel.bannerTitle
        homeBinding.indicator = homeBinding.ppIndicator // can we simplify?
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

        //1.仅获取Banner
        bannerModel.bannersLiveData.observe(this,
            Observer<Resource<List<BannerItem>>> {
                homeBinding.adapter!!.refresh(homeBinding.vpBanner,it.data)
                bannerModel.refresh()
                homeBinding.vpBanner.startAutoSwitch()
                Log.d(javaClass.simpleName,"${it.status}, ${it.data}, ${it.msg}")
            })

        // MergeCall 合并请求
        //2.同时获取置顶文章接口数据和首页文章指定页数据，但不携带Banner数据
//        bannerModel.articleLiveData.observe(this, Observer {
//            Log.e("hudson","articles: $it")
//        })

        // MergeCall 合并请求
        // 3.同时获取Banner + 置顶文章 + 首页文章数据
        bannerModel.homeAllDataLiveData.observe(this, Observer {
            Log.e("hudson","home all data: $it")
        })

    }
}