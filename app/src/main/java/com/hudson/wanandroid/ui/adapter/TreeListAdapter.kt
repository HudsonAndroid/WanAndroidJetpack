package com.hudson.wanandroid.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.TreeItem
import com.hudson.wanandroid.databinding.ItemTreeItemBinding
import com.hudson.wanandroid.ui.adapter.viewholder.TreeItemViewHolder
import com.hudson.wanandroid.viewmodel.bindingadapter.TreeClickListener

/**
 * Created by Hudson on 2020/8/24.
 */
class TreeListAdapter(private val itemClickListener: TreeClickListener)
    : RecyclerView.Adapter<TreeItemViewHolder>(){
    private val treeList = mutableListOf<TreeItem>()

    fun refresh(trees: List<TreeItem>){
        treeList.clear()
        treeList.addAll(trees)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreeItemViewHolder {
        val binding =
            ItemTreeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TreeItemViewHolder(binding, itemClickListener)
    }

    override fun getItemCount() = treeList.size

    override fun onBindViewHolder(holder: TreeItemViewHolder, position: Int) {
        holder.bindTree(treeList[position])
    }

}