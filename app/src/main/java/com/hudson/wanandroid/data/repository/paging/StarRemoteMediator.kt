package com.hudson.wanandroid.data.repository.paging

import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.StarArticle

/**
 * Created by Hudson on 2020/8/30.
 */
class StarRemoteMediator(
    api: WanAndroidApi,
    db: WanAndroidDb
    ): BaseRemoteMediator<StarArticle>(api, db){
    private var nextKey: Int? = null

    override suspend fun fetchNetworkData(nextPageKey: Int?): List<StarArticle> {
        return nextPageKey?.run {
            val result = api.starArticlesResult(nextPageKey)
            nextKey = result.data.curPage + 1
            result.data.datas
        } ?: mutableListOf()
    }

    override fun getNextKey(nextPageKey: Int?) = nextKey

    override suspend fun updateNetworkData(db: WanAndroidDb, data: List<StarArticle>)
        = db.starArticleDao().insertStarArticles(data)

    override suspend fun cleanLocalData(db: WanAndroidDb)
        = db.starArticleDao().cleanArticles()
}