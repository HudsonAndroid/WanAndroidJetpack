package com.hudson.wanandroid.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.repository.ProjectsRepository
import javax.inject.Inject

/**
 * Created by Hudson on 2020/8/17.
 */
class ProjectItemModel @Inject constructor(private val repository: ProjectsRepository): ViewModel(){

    val loadState = MutableLiveData<PagingRetryLoad>()

    fun loadData(projectId: Int) = repository.loadProjectItemArticles(projectId).cachedIn(viewModelScope)

    suspend fun starOrReverseArticle(context: Context, article: Article) {
        repository.starOrReverseArticle(context, article)
    }
}