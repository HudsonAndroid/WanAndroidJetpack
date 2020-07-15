package com.hudson.wanandroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        findViewById<BottomNavigationView>(R.id.bnv_navigation).setupWithNavController(navController)
//
//        var contentView = DataBindingUtil.setContentView<FragmentHomeBinding>(this, R.layout.fragment_home)
//        var bannerModel = BannerModel()
//        bannerModel.bindViewPager(contentView.vpBanner)
//        bannerModel.indicator = contentView.ppIndicator
//        contentView.banner = bannerModel
//        contentView.lifecycleOwner = this //自动为LiveData设置数据观察绑定，相当于liveData.observe自动完成

    }
}
