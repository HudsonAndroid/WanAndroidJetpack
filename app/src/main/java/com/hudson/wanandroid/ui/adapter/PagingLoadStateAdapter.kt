package com.hudson.wanandroid.ui.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import com.hudson.wanandroid.ui.adapter.viewholder.LoadStateViewHolder

/**
 * Created by Hudson on 2020/7/30.
 */
open class PagingLoadStateAdapter<T: PagingDataAdapter<*,*>>(
    private val adapter: T
): LoadStateAdapter<LoadStateViewHolder>(){

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.synchronizeLoadState(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(parent){
            adapter.retry()
        }
    }

}