package com.hudson.wanandroid.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.NavigationItem
import com.hudson.wanandroid.databinding.ItemNavigationItemBinding
import com.hudson.wanandroid.viewmodel.global.GlobalJumpHandler

/**
 * Created by Hudson on 2020/8/25.
 */
class NavigationViewHolder(private val binding: ItemNavigationItemBinding)
    :RecyclerView.ViewHolder(binding.root){

    fun bindNavigation(navigationItem: NavigationItem){
        binding.navigation = navigationItem
        binding.clickListener = View.OnClickListener {
            val tag = it.tag
            if(tag != null && tag is Article){
                GlobalJumpHandler.onJumpInvoked(it, tag.link)
            }
        }
    }
}