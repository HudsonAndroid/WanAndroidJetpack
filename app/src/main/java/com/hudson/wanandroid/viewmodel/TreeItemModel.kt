package com.hudson.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.repository.TreeRepository
import javax.inject.Inject

/**
 * Created by Hudson on 2020/8/25.
 */
class TreeItemModel @Inject constructor(private val repository: TreeRepository): ViewModel(){
    val loadState = MutableLiveData<PagingRetryLoad>()

    fun loadArticles(treeId: Int, superId: Int)
            = repository.loadTreeItemArticles(treeId, superId).cachedIn(viewModelScope)
}