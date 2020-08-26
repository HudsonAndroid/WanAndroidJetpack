package com.hudson.wanandroid.ui.adapter

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hudson.wanandroid.data.entity.TreeItem
import com.hudson.wanandroid.ui.fragment.tree.TreeArticleFragment

/**
 * Created by Hudson on 2020/8/25.
 */
class TreeListPagerAdapter(activity: FragmentActivity,
    private val treeList: List<TreeItem>): FragmentStateAdapter(activity){

    override fun getItemCount() = treeList.size

    override fun createFragment(position: Int) = TreeArticleFragment(treeList[position])
}