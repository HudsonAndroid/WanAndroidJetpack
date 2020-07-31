package com.hudson.wanandroid.ui.adapter.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.R

/**
 * Created by Hudson on 2020/7/30.
 */
class LoadStateViewHolder (
    parent: ViewGroup,
    private val retryCallback: ()-> Unit
): RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_paging_load_state,parent,false)
){

    private val progressBar = itemView.findViewById<ProgressBar>(R.id.pb_load)
    private val errorMsg = itemView.findViewById<TextView>(R.id.tv_msg)
    private val retry = itemView.findViewById<Button>(R.id.btn_retry).also {
        it.setOnClickListener{
            retryCallback()
        }
    }


    fun synchronizeLoadState(loadState: LoadState){
        Log.e("hudson", "加载状态"+loadState)
        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
    }
}