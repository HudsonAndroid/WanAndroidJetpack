package com.hudson.wanandroid.viewmodel.bindingadapter

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.BindingAdapter
import com.hudson.wanandroid.data.entity.Subject
import com.hudson.wanandroid.databinding.ItemRoundWordBinding
import com.hudson.wanandroid.ui.view.AutoAdaptLayout

/**
 * Created by Hudson on 2020/8/25.
 */
@BindingAdapter(value = ["targetTree","treeClickListener"], requireAll = false)
fun targetTree(container: AutoAdaptLayout, tree: Subject?,
               clickListener: TreeClickListener?){
    if(tree != null && tree.children.isNotEmpty()){
        container.removeAllViews()
        tree.children.forEachIndexed { index, treeItem ->
            val binding = ItemRoundWordBinding.inflate(
                LayoutInflater.from(container.context),
                container,
                true)
            binding.word = treeItem.name
            binding.clickListener = View.OnClickListener {
                clickListener?.onClick(tree, index)
            }
        }
    }
}

interface TreeClickListener{
    fun onClick(targetTree: Subject, selectedIndex: Int)
}