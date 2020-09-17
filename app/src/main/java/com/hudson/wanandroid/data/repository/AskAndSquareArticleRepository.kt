package com.hudson.wanandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.repository.paging.AskArticleRemoteMediator
import com.hudson.wanandroid.data.repository.paging.SquareArticleRemoteMediator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/9/17.
 */
@Singleton
class AskAndSquareArticleRepository @Inject constructor(
    wanAndroidApi: WanAndroidApi,
    db: WanAndroidDb
): ArticleRepository(wanAndroidApi, db){
    companion object{
        private const val NETWORK_PAGE_SIZE = 20
    }

    fun loadAskArticles() = Pager(config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        remoteMediator = AskArticleRemoteMediator(wanAndroidApi, db)){
        db.articleDao().getSubjectPagingSource(AskArticleRemoteMediator.CHAPTER_ID, AskArticleRemoteMediator.SUPER_ID)
    }.flow

    fun loadSquareArticles() = Pager(config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        remoteMediator = SquareArticleRemoteMediator(wanAndroidApi, db)){
        db.articleDao().getSubjectPagingSource(SquareArticleRemoteMediator.CHAPTER_ID, SquareArticleRemoteMediator.SUPER_ID)
    }.flow
}