package com.hudson.wanandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hudson.wanandroid.databinding.PageHomeBinding
import com.hudson.wanandroid.viewmodel.BannerModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.activity_main)
        var contentView = DataBindingUtil.setContentView<PageHomeBinding>(this, R.layout.page_home)
        var bannerModel = BannerModel()
        bannerModel.bindViewPager(contentView.vpBanner)
        bannerModel.indicator = contentView.ppIndicator
        contentView.banner = bannerModel
        contentView.lifecycleOwner = this //自动为LiveData设置数据观察绑定，相当于liveData.observe自动完成

    }
}
