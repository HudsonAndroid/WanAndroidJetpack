package com.hudson.wanandroid.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.databinding.ItemAccountBinding

/**
 * Created by Hudson on 2020/9/1.
 */
class AccountViewHolder(private val binding: ItemAccountBinding,
        private val itemClickListener: OnUserItemClickListener):
        RecyclerView.ViewHolder(binding.root){

    fun bindAccount(loginUser: LoginUser){
        binding.account = loginUser
        binding.clRoot.setOnClickListener {
            itemClickListener.onClick(loginUser)
        }
    }
}

interface OnUserItemClickListener{
    fun onClick(loginUser: LoginUser)
}