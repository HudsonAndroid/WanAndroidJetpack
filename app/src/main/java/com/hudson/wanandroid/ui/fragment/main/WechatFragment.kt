package com.hudson.wanandroid.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.hudson.wanandroid.data.account.AccountRelative
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.wrapper.Status
import com.hudson.wanandroid.databinding.FragmentWechatBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.WechatPagerAdapter
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.WechatModel
import javax.inject.Inject

/**
 * 公众号
 */
class WechatFragment : Fragment(), Injectable, AccountRelative {
    companion object{
        const val CACHE_FRAGMENT_SIZE = 2
    }
    private var wechatBinding by autoCleared<FragmentWechatBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val wechatModel: WechatModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        wechatBinding = FragmentWechatBinding.inflate(inflater, container, false)
        return wechatBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        wechatBinding.category = wechatModel.wechatCategory
        wechatBinding.lifecycleOwner = this
        wechatBinding.retry = object : RetryCallback{
            override fun retry() {
                wechatModel.retry()
            }
        }

        wechatModel.wechatCategory.observe(viewLifecycleOwner, Observer {
            if(it.status == Status.SUCCESS && it.data != null){
                wechatBinding.viewPager.adapter = WechatPagerAdapter(this, it.data)
                wechatBinding.viewPager.offscreenPageLimit = CACHE_FRAGMENT_SIZE
                TabLayoutMediator(wechatBinding.tabs, wechatBinding.viewPager){ tab, position ->
                    tab.text = it.data[position].name
                }.attach()
            }
        })
    }

    override fun onAccountChanged(user: LoginUser?) {
        wechatModel.refresh()
    }
}
