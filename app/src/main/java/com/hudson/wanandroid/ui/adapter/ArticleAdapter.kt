package com.hudson.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.databinding.ItemPagingArticleBinding
import com.hudson.wanandroid.ui.adapter.viewholder.HomeArticleViewHolder

/**
 * Created by Hudson on 2020/7/30.
 */
class ArticleAdapter : PagingDataAdapter<Article, HomeArticleViewHolder>(DiffCallback){

    companion object{
        val DiffCallback = object: DiffUtil.ItemCallback<Article>(){
            override fun areItemsTheSame(oldItem: Article, newItem: Article) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article) =
                oldItem == newItem

        }
    }

    override fun onBindViewHolder(holder: HomeArticleViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bindArticle(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeArticleViewHolder {
        val binding =
            ItemPagingArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeArticleViewHolder(binding)
    }
}