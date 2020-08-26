package com.hudson.wanandroid.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hudson.wanandroid.ui.fragment.tree.DefaultTreeFragment
import com.hudson.wanandroid.ui.fragment.tree.NavigateFragment

const val DEFAULT_TREE_INDEX = 0
const val NAVIGATION_INDEX = 1
/**
 * Created by Hudson on 2020/8/24.
 */
class TreePagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment){

    private val tabFragmentCreators: Map<Int, () -> Fragment> = mapOf(
        DEFAULT_TREE_INDEX to { DefaultTreeFragment() },
        NAVIGATION_INDEX to { NavigateFragment() }
    )

    override fun getItemCount() = tabFragmentCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}