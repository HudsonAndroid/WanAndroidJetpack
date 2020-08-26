package com.hudson.wanandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.repository.TreeRepository
import javax.inject.Inject

/**
 * Created by Hudson on 2020/8/24.
 */
class TreeModel @Inject constructor(private val repository: TreeRepository): ViewModel() {

    val treeList = repository.loadTree()

    fun retry() {
        repository.retry()
    }
}