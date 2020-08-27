package com.hudson.wanandroid.data.repository.paging

import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Article

/**
 * Created by Hudson on 2020/8/26.
 */
class WechatRemoteMediator(
    api: WanAndroidApi,
    db: WanAndroidDb,
    private val wechatId: Int,
    private val superId: Int
): BaseRemoteMediator<Article>(api, db){
    private var nextKey: Int? = null

    override suspend fun fetchNetworkData(nextPageKey: Int?): List<Article> {
        return nextPageKey?.run {
            val result = api.wechatItemList(wechatId, nextPageKey)
            nextKey = result.data.curPage + 1
            result.data.datas
        } ?: mutableListOf()
    }

    override fun getDefaultStartPageKey() = 1

    override fun getNextKey(nextPageKey: Int?) = nextKey

    override suspend fun updateNetworkData(db: WanAndroidDb, data: List<Article>){
        db.articleDao().insertArticles(data)
    }

    override suspend fun cleanLocalData(db: WanAndroidDb) = db.articleDao().cleanTargetArticles(wechatId, superId)

    override fun getPageKeyType(): String {
        return super.getPageKeyType() + "_wechat_$wechatId"
    }

}