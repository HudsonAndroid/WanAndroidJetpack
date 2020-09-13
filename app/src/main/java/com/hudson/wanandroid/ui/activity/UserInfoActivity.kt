package com.hudson.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.account.AccountRelative
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.wrapper.Status
import com.hudson.wanandroid.databinding.ActivityUserInfoBinding
import com.hudson.wanandroid.ui.adapter.UserInfoPagerAdapter
import com.hudson.wanandroid.viewmodel.UserScoreModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class UserInfoActivity : AppCompatActivity(), HasSupportFragmentInjector, AccountRelative {
    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory
    private lateinit var binding:ActivityUserInfoBinding

    private val userScoreModel: UserScoreModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityUserInfoBinding>(
            this,
            R.layout.activity_user_info
        )

        // set adapter
        val userInfoPagerAdapter = UserInfoPagerAdapter(this)
        binding.viewPager.adapter = userInfoPagerAdapter
        // attach
        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, position ->
            tab.text = getString(userInfoPagerAdapter.getTitleStrId(position))
        }.attach()
    }


    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, UserInfoActivity::class.java))
        }
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onAccountChanged(user: LoginUser?) {
        binding.currentUser = user
        user?.id?.run {
            userScoreModel.loadCurrentUserScore(this).observe(this@UserInfoActivity, Observer {
                // we only care successful result
                if(it.status == Status.SUCCESS){
                    binding.userScore = it.data
                }
            })
        }
    }
}
