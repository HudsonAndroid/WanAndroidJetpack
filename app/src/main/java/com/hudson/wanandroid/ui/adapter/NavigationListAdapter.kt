package com.hudson.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.NavigationItem
import com.hudson.wanandroid.databinding.ItemNavigationItemBinding
import com.hudson.wanandroid.ui.adapter.viewholder.NavigationViewHolder

/**
 * Created by Hudson on 2020/8/25.
 */
class NavigationListAdapter() : RecyclerView.Adapter<NavigationViewHolder>(){
    private val navigationList = mutableListOf<NavigationItem>()

    fun refresh(input: List<NavigationItem>){
        navigationList.clear()
        navigationList.addAll(input)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationViewHolder {
        val binding =
            ItemNavigationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NavigationViewHolder(binding)
    }

    override fun getItemCount() = navigationList.size

    override fun onBindViewHolder(holder: NavigationViewHolder, position: Int) {
        holder.bindNavigation(navigationList[position])
    }

}