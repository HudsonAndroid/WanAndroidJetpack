package com.hudson.wanandroid.data.repository.paging

import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Article

/**
 * Created by Hudson on 2020/8/25.
 */
class TreeItemRemoteMediator(
    api: WanAndroidApi,
    db: WanAndroidDb,
    private val treeId: Int,
    private val superId: Int
) : BaseRemoteMediator<Article>(api,db){
    private var nextKey: Int? = null

    override suspend fun fetchNetworkData(nextPageKey: Int?): List<Article> {
        return nextPageKey?.run {
            val result = api.treeItemList(nextPageKey, treeId)
            nextKey = result.data.curPage + 1
            result.data.datas
        } ?: mutableListOf()
    }

    override fun getNextKey(nextPageKey: Int?) = nextKey

    override suspend fun updateNetworkData(db: WanAndroidDb, data: List<Article>)
            = db.articleDao().insertArticles(data)

    override suspend fun cleanLocalData(db: WanAndroidDb)
            = db.articleDao().cleanTargetArticles(treeId, superId)

    override fun getPageKeyType(): String {
        return super.getPageKeyType() + "_tree_$treeId"
    }

}