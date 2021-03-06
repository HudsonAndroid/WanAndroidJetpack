package com.hudson.wanandroid.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.databinding.ItemPagingArticleBinding

/**
 * Created by Hudson on 2020/7/30.
 */
class ArticleViewHolder(private val binding: ItemPagingArticleBinding,
    private val starClickListener: ArticleStarClickListener? = null):
    RecyclerView.ViewHolder(binding.root){

    fun bindArticle(article: Article){
        binding.article = article
        binding.ivStar.setOnClickListener {
            starClickListener?.onStarClick(article, layoutPosition)
        }
    }
}

interface ArticleStarClickListener{
    fun onStarClick(article: Article, position: Int)
}