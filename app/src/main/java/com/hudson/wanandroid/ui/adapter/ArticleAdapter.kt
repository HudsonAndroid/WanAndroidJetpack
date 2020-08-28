package com.hudson.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.databinding.ItemPagingArticleBinding
import com.hudson.wanandroid.ui.adapter.viewholder.ArticleStarClickListener
import com.hudson.wanandroid.ui.adapter.viewholder.ArticleViewHolder

/**
 * Created by Hudson on 2020/7/30.
 */
class ArticleAdapter(private val starClickListener: ArticleStarClickListener? = null)
    : PagingDataAdapter<Article, ArticleViewHolder>(DiffCallback){

    companion object{
        val DiffCallback = object: DiffUtil.ItemCallback<Article>(){
            override fun areItemsTheSame(oldItem: Article, newItem: Article) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Article, newItem: Article) =
                oldItem == newItem

        }
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bindArticle(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =
            ItemPagingArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding, starClickListener)
    }
}