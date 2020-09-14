package com.hudson.wanandroid.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.StarArticle
import com.hudson.wanandroid.databinding.ItemPagingStarArticleBinding

/**
 * Created by Hudson on 2020/7/30.
 */
class StarArticleViewHolder(private val binding: ItemPagingStarArticleBinding,
    private val starClickListener: StarClickListener? = null):
    RecyclerView.ViewHolder(binding.root){

    fun bindArticle(article: StarArticle){
        binding.article = article
        binding.ivStar.setOnClickListener {
            starClickListener?.onStarClick(article, layoutPosition)
        }
    }
}

interface StarClickListener{
    fun onStarClick(article: StarArticle, position: Int)
}