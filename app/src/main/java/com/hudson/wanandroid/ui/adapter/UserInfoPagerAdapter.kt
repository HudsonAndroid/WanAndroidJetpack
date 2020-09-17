package com.hudson.wanandroid.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hudson.wanandroid.R
import com.hudson.wanandroid.ui.fragment.userinfo.UserScoreFragment
import com.hudson.wanandroid.ui.fragment.userinfo.UserStarFragment

const val DEFAULT_SCORE = 0
const val USER_STAR = 1

/**
 * Created by Hudson on 2020/9/12.
 */
class UserInfoPagerAdapter(activity: FragmentActivity)
    : FragmentStateAdapter(activity){

    private val tabFragmentCreators: Map<Int, () -> Fragment> = mapOf(
        DEFAULT_SCORE to { UserScoreFragment() },
        USER_STAR to { UserStarFragment() }
    )

    override fun getItemCount() = tabFragmentCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

    fun getTitleStrId(index: Int): Int{
        return when (index) {
            DEFAULT_SCORE -> {
                R.string.title_score_board
            }
            USER_STAR -> {
                R.string.title_my_star
            }
            else -> {
                throw java.lang.IndexOutOfBoundsException()
            }
        }
    }
}