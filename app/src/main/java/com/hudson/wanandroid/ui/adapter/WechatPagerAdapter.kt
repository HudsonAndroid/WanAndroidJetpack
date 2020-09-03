package com.hudson.wanandroid.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hudson.wanandroid.data.entity.Subject
import com.hudson.wanandroid.ui.fragment.wechat.WechatItemFragment

/**
 * Created by Hudson on 2020/8/26.
 */
class WechatPagerAdapter(fragment: Fragment,
                private val categories: List<Subject>)
    : FragmentStateAdapter(fragment){
    override fun getItemCount() = categories.size

    override fun createFragment(position: Int) = WechatItemFragment.newInstance(categories[position])

}