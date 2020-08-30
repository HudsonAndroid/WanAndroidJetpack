package com.hudson.wanandroid.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
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
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,
    NavigationView.OnNavigationItemSelectedListener {

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

        configDrawer()
    }

    private fun configDrawer(){
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawer: DrawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.menu_open, R.string.menu_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menu_search){
            SearchActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.nav_score){
            LoginActivity.start(this)
//            SearchActivity.start(this)
        }
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//        } else if (id == R.id.nav_slideshow) {
//        } else if (id == R.id.nav_manage) {
//        } else if (id == R.id.nav_share) {
//        } else if (id == R.id.nav_send) {
//        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
