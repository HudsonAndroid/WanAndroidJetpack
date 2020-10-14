package com.hudson.wanandroid.viewmodel.bindingadapter

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.BindingAdapter
import com.hudson.wanandroid.data.entity.UserWebsiteItem
import com.hudson.wanandroid.databinding.ItemRoundWordBinding
import com.hudson.wanandroid.ui.view.AutoAdaptLayout

/**
 * Created by Hudson on 2020/9/18.
 */
@BindingAdapter(value = ["websites", "websiteClickListener"], requireAll = false)
fun websites(container: AutoAdaptLayout, websites: List<UserWebsiteItem>?,
             websiteClickListener: WebsiteClickListener?){
    if(websites != null && websites.isNotEmpty()){
        container.removeAllViews()
        val target: List<UserWebsiteItem> = websites.reversed()
        target.forEach {
            val websiteItem = it
            val binding =
                ItemRoundWordBinding.inflate(LayoutInflater.from(container.context), container, true)
            binding.clickListener = View.OnClickListener {
                websiteClickListener?.onWebsiteClick(websiteItem)
            }
            binding.word = it.name
        }
    }
}

interface WebsiteClickListener {
    fun onWebsiteClick(userWebsiteItem: UserWebsiteItem)
}