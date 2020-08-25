package com.hudson.wanandroid.viewmodel.bindingadapter

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.BindingAdapter
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.databinding.ItemRoundWordBinding
import com.hudson.wanandroid.ui.view.AutoAdaptLayout

/**
 * Created by Hudson on 2020/8/25.
 */
@BindingAdapter(value = ["navigationList","navigationClickListener"], requireAll = false)
fun navigationList(container: AutoAdaptLayout, articles: List<Article>?,
        clickListener: View.OnClickListener?){
    if(articles != null && articles.isNotEmpty()){
        container.removeAllViews()
        articles.forEach {
            val binding = ItemRoundWordBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                true)
            binding.clickListener = clickListener
            binding.word = it.title
            binding.tvWord.tag = it
        }
    }
}