package com.hudson.wanandroid.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.SearchEngine
import com.hudson.wanandroid.databinding.ItemSearchEngineBinding

/**
 * Created by Hudson on 2020/9/25.
 */
class SearchEngineViewHolder(private val binding: ItemSearchEngineBinding,
                             private val searchEngineClickListener: OnSearchEngineClickListener
): RecyclerView.ViewHolder(binding.root){

    fun bindSearchEngine(searchEngine: SearchEngine){
        binding.engine = searchEngine
        binding.clRoot.setOnClickListener {
            searchEngineClickListener.onClick(searchEngine)
        }
    }
}

interface OnSearchEngineClickListener {
    fun onClick(engine: SearchEngine)
}