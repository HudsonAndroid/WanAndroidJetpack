package com.hudson.wanandroid.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.StarArticle
import com.hudson.wanandroid.data.repository.ArticleStarRepository
import javax.inject.Inject

/**
 * Created by Hudson on 2020/8/30.
 */
class StarModel @Inject constructor(private val repository: ArticleStarRepository)
    : ViewModel(){

    val loadState = MutableLiveData<PagingRetryLoad>()

    fun loadStarArticles() = repository.loadStarArticles()

    suspend fun unStarArticle(context: Context, article: StarArticle) {
        repository.unStarArticle(context, article)
    }
}