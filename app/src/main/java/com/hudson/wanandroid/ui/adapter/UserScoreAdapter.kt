package com.hudson.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.hudson.wanandroid.data.entity.UserScore
import com.hudson.wanandroid.databinding.ItemUserScoreBinding
import com.hudson.wanandroid.ui.adapter.viewholder.UserScoreViewHolder

/**
 * Created by Hudson on 2020/9/13.
 */
class UserScoreAdapter()
    :PagingDataAdapter<UserScore, UserScoreViewHolder>(DiffCallback){

    companion object{
        val DiffCallback = object: DiffUtil.ItemCallback<UserScore>(){
            override fun areItemsTheSame(oldItem: UserScore, newItem: UserScore)
                    = oldItem.userId == newItem.userId

            override fun areContentsTheSame(oldItem: UserScore, newItem: UserScore)
                    = oldItem == newItem

        }
    }

    override fun onBindViewHolder(holder: UserScoreViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bindUserScore(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserScoreViewHolder {
        val binding =
            ItemUserScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserScoreViewHolder(binding)
    }

}