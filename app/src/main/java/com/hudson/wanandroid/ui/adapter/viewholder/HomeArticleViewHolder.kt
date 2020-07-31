package com.hudson.wanandroid.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.databinding.ItemPagingArticleBinding

/**
 * Created by Hudson on 2020/7/30.
 */
class HomeArticleViewHolder(private val binding: ItemPagingArticleBinding):
    RecyclerView.ViewHolder(binding.root){

    fun bindArticle(article: Article){
        binding.article = article
    }
}