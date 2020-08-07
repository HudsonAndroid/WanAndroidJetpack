package com.hudson.wanandroid.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hudson.wanandroid.R
import com.hudson.wanandroid.ui.fix.WanAndroidNavigator
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * 与App类似，内部需要维护旗下的Fragment注入器的集合
 *
 * 在Fragment注入时会自动找到Activity，并利用下面的dispatchingAndroidInjector来完成注入
 */
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = findNavController(R.id.nav_host_fragment)
        // 使用自定义的Navigator
        val navigator = WanAndroidNavigator(
            this,
            navHostFragment!!.childFragmentManager,
            R.id.nav_host_fragment
        )

        navController.navigatorProvider.addNavigator(navigator)

        navController.setGraph(R.navigation.nav_graph_main)
        findViewById<BottomNavigationView>(R.id.bnv_navigation).setupWithNavController(navController)

    }
}
