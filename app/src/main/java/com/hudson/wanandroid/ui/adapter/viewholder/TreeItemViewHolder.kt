package com.hudson.wanandroid.ui.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.hudson.wanandroid.data.entity.TreeItem
import com.hudson.wanandroid.databinding.ItemTreeItemBinding
import com.hudson.wanandroid.viewmodel.bindingadapter.TreeClickListener

/**
 * Created by Hudson on 2020/8/24.
 */
class TreeItemViewHolder(private val binding: ItemTreeItemBinding,
        private val clickListener: TreeClickListener?):
        RecyclerView.ViewHolder(binding.root){

    fun bindTree(tree: TreeItem){
        binding.tree = tree
        binding.clickListener = clickListener
    }
}