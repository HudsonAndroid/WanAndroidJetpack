package com.hudson.wanandroid.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.UserScore
import com.hudson.wanandroid.databinding.ItemUserScoreBinding

/**
 * Created by Hudson on 2020/9/13.
 */
class UserScoreViewHolder(private val binding: ItemUserScoreBinding)
    : RecyclerView.ViewHolder(binding.root){

    fun bindUserScore(userScore: UserScore){
        binding.userScore = userScore
        binding.rankNo = layoutPosition + 1
    }
}