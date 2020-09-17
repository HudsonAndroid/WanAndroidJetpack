package com.hudson.wanandroid.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.repository.AskAndSquareArticleRepository
import javax.inject.Inject

/**
 * Created by Hudson on 2020/9/17.
 */
class SquareArticleModel @Inject constructor(private val repository: AskAndSquareArticleRepository)
    : ViewModel(){

    val loadState = MutableLiveData<PagingRetryLoad>()

    fun loadSquareArticles() = repository.loadSquareArticles().cachedIn(viewModelScope)

    suspend fun starOrReverseArticle(context: Context, article: Article) {
        repository.starOrReverseArticle(context, article)
    }
}