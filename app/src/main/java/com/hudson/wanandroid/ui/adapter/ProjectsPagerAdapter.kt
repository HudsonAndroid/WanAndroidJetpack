package com.hudson.wanandroid.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hudson.wanandroid.data.entity.ProjectInfo
import com.hudson.wanandroid.ui.fragment.projects.ProjectItemFragment

/**
 * 继承自FragmentStateAdapter情况下，
 * Fragment在超出边界个数会被销毁，但有个问题是
 * 重新回到该页面时，RecyclerView也会被重置，
 * 即滑动位置也变了，这体验相对更差，但内存效率高；
 * 继承自MyFragmentStateAdapter情况下，
 * 由于不再移除Fragment，因此不会被重置，但是会
 * 缓存所有Fragment，内存效率很低，容易OOM。为了
 * 更好的体验，可以略微调大offscreenPageLimit的值
 *
 *
 * 或许更好的处理是，SaveState不移除（在FragmentStateAdapter中
 * 会同时移除SaveState），且SaveState需要保存RecyclerView的滚动
 * 位置，并在数据加载时恢复滚动到指定位置，但是有个问题，Paging是
 * 分页加载的，滚动可能最多只能滚动到第一页的底部
 * Created by Hudson on 2020/8/17.
 */
class ProjectsPagerAdapter(fragment: Fragment,
                           private val projects: List<ProjectInfo>)
    : FragmentStateAdapter(fragment){

    override fun getItemCount() = projects.size

    override fun createFragment(position: Int): ProjectItemFragment {
        return ProjectItemFragment(projects[position])
    }

}