package com.hudson.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.SearchEngine
import com.hudson.wanandroid.databinding.ItemSearchEngineBinding
import com.hudson.wanandroid.ui.adapter.viewholder.OnSearchEngineClickListener
import com.hudson.wanandroid.ui.adapter.viewholder.SearchEngineViewHolder

/**
 * Created by Hudson on 2020/9/25.
 */
class SearchEngineAdapter(private val searchEngineClickListener: OnSearchEngineClickListener)
    :RecyclerView.Adapter<SearchEngineViewHolder>(){
    private val engineList = mutableListOf<SearchEngine>()

    fun refresh(engines: List<SearchEngine>){
        engineList.clear()
        engineList.addAll(engines)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchEngineViewHolder {
        val binding =
            ItemSearchEngineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchEngineViewHolder(binding, searchEngineClickListener)
    }

    override fun getItemCount() = engineList.size

    override fun onBindViewHolder(holder: SearchEngineViewHolder, position: Int) {
        holder.bindSearchEngine(engineList[position])
    }

}