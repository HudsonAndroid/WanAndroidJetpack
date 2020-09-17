package com.hudson.wanandroid.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.account.AccountRelative
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.wrapper.Status
import com.hudson.wanandroid.databinding.NavHeaderSideBinding
import com.hudson.wanandroid.ui.activity.ArticleActivity.Companion.TYPE_ASK
import com.hudson.wanandroid.ui.activity.ArticleActivity.Companion.TYPE_SQUARE
import com.hudson.wanandroid.ui.fix.WanAndroidNavigator
import com.hudson.wanandroid.viewmodel.UserScoreModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * 与App类似，内部需要维护旗下的Fragment注入器的集合
 *
 * 在Fragment注入时会自动找到Activity，并利用下面的dispatchingAndroidInjector来完成注入
 */
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector,
    NavigationView.OnNavigationItemSelectedListener, AccountRelative {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    lateinit var navigationView: NavigationView

    private val userScoreModel: UserScoreModel by viewModels {
        viewModelFactory
    }

    lateinit var headerSideBinding: NavHeaderSideBinding

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

        initTheme()
    }

    private fun initTheme(){
        val configuration = getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE)
        val isCustomNightMode = configuration.getBoolean(CUSTOM_NIGHT_FLAG, false)
        val menuItem = navigationView.menu.findItem(R.id.nav_night)
        if(isCustomNightMode){
            // 使用自定义的夜间模式
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            menuItem.setTitle(R.string.menu_night_system)
        }
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

        navigationView = findViewById<NavigationView>(R.id.nav_view)

        headerSideBinding = NavHeaderSideBinding.inflate(layoutInflater, navigationView, false)
        navigationView.addHeaderView(headerSideBinding.root)
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
        if(id == R.id.nav_ask){
            ArticleActivity.start(this, TYPE_ASK)
        }else if(id == R.id.nav_night){
            // Day-Night theme switch see
            // https://medium.com/androiddevelopers/appcompat-v23-2-daynight-d10f90c83e94
            val configuration = getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE)
            val edit = configuration.edit()
            val isCustomNightMode = configuration.getBoolean(CUSTOM_NIGHT_FLAG, false)
            val menuItem = navigationView.menu.findItem(R.id.nav_night)
            if(isCustomNightMode){
                // 跟随系统
                // 注意：下面方法对AppCompatActivity生效，其他类型的Activity无法生效
                // 另外可以通过setLocalNightMode单独对某个Activity应用
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                menuItem.setTitle(R.string.menu_night_system)
            }else{
                // 使用自定义的夜间模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                menuItem.setTitle(R.string.menu_night)
            }
            edit.putBoolean(CUSTOM_NIGHT_FLAG, !isCustomNightMode).apply()
        }else if(id == R.id.nav_square){
            ArticleActivity.start(this, TYPE_SQUARE)
        }

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

    override fun onAccountChanged(user: LoginUser?) {
        headerSideBinding.currentUser = user
        user?.id?.run {
            userScoreModel.loadCurrentUserScore(this).observe(this@MainActivity, Observer {
                // we only care successful result
                if(it.status == Status.SUCCESS){
                    headerSideBinding.userScore = it.data
                }
            })
        }
    }

    companion object{
        const val CONFIG_NAME = "wanandroidConfigs"
        const val CUSTOM_NIGHT_FLAG = "customNightFlag"
    }
}
