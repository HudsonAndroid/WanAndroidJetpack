package com.hudson.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hudson.wanandroid.data.entity.StarArticle
import com.hudson.wanandroid.databinding.ItemPagingStarArticleBinding
import com.hudson.wanandroid.ui.adapter.viewholder.StarArticleViewHolder
import com.hudson.wanandroid.ui.adapter.viewholder.StarClickListener

/**
 * Created by Hudson on 2020/7/30.
 */
class StarArticleAdapter(private val starClickListener: StarClickListener? = null)
    : PagingDataAdapter<StarArticle, StarArticleViewHolder>(DiffCallback){

    companion object{
        val DiffCallback = object: DiffUtil.ItemCallback<StarArticle>(){
            override fun areItemsTheSame(oldItem: StarArticle, newItem: StarArticle): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StarArticle, newItem: StarArticle): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onBindViewHolder(holder: StarArticleViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bindArticle(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarArticleViewHolder {
        val binding =
            ItemPagingStarArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StarArticleViewHolder(binding, starClickListener)
    }
}