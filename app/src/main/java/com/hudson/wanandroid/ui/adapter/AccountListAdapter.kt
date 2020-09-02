package com.hudson.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.databinding.ItemAccountBinding
import com.hudson.wanandroid.ui.adapter.viewholder.AccountViewHolder
import com.hudson.wanandroid.ui.adapter.viewholder.OnUserItemClickListener

/**
 * Created by Hudson on 2020/9/1.
 */
class AccountListAdapter(private val userItemClickListener: OnUserItemClickListener)
    : RecyclerView.Adapter<AccountViewHolder>(){
    private val accountList = mutableListOf<LoginUser>()

    fun load(accounts: List<LoginUser>){
        accountList.clear()
        accountList.addAll(accounts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding, userItemClickListener)
    }

    override fun getItemCount() = accountList.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bindAccount(accountList[position])
    }

}