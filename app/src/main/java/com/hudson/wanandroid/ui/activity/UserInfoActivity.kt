package com.hudson.wanandroid.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.account.AccountRelative
import com.hudson.wanandroid.data.account.WanAndroidAccount
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.wrapper.Status
import com.hudson.wanandroid.databinding.ActivityUserInfoBinding
import com.hudson.wanandroid.ui.adapter.UserInfoPagerAdapter
import com.hudson.wanandroid.viewmodel.UserScoreModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.coroutines.launch
import timber.log.Timber
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
        binding.ivIcon.setOnClickListener {
            chooseImage()
        }
        // attach
        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, position ->
            tab.text = getString(userInfoPagerAdapter.getTitleStrId(position))
        }.attach()
    }

    private fun chooseImage(){
        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(pickIntent, "Pictures: "), PICK_IMAGE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_CODE){
            Timber.e("${data?.data}")
            data?.data?.run {
                val me = this
                lifecycleScope.launch {
                    WanAndroidAccount.getInstance().updateAvatar(this@UserInfoActivity, me)
                }
            }
        }
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, UserInfoActivity::class.java))
        }
        const val PICK_IMAGE_CODE = 0x666
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
