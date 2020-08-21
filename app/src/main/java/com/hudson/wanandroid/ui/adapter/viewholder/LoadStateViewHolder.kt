package com.hudson.wanandroid.ui.adapter.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    LayoutInflater.from(parent.context).inflate(R.layout.item_paging_footer_load_state,parent,false)
){
    private val progressBar = itemView.findViewById<ProgressBar>(R.id.pb_load)
    private val errorMsg = itemView.findViewById<TextView>(R.id.tv_msg)

    init {
        itemView.findViewById<View>(R.id.cl_load_state).also {
            it.setOnClickListener{
                retryCallback()
            }
        }
    }

    fun synchronizeLoadState(loadState: LoadState){
        progressBar.isVisible = loadState is LoadState.Loading
        errorMsg.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
    }
}

