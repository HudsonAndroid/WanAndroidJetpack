package com.hudson.wanandroid.data.repository.paging

import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Article

/**
 * Created by Hudson on 2020/9/17.
 */
class SquareArticleRemoteMediator(
    api: WanAndroidApi,
    db: WanAndroidDb
): BaseRemoteMediator<Article>(api, db){
    companion object{
        const val CHAPTER_ID = 494
        const val SUPER_ID = 493
    }
    private var nextKey: Int? = null

    override suspend fun fetchNetworkData(nextPageKey: Int?): List<Article> {
        return nextPageKey?.run {
            val result = api.squareArticles(this)
            nextKey = result.data.curPage + 1
            result.data.datas
        } ?: mutableListOf()
    }

    override fun getDefaultStartPageKey() = 0

    override fun getNextKey(nextPageKey: Int?) = nextKey

    override suspend fun updateNetworkData(db: WanAndroidDb, data: List<Article>){
        db.articleDao().insertArticles(data)
    }

    override suspend fun cleanLocalData(db: WanAndroidDb)
            = db.articleDao().cleanTargetArticles(CHAPTER_ID, SUPER_ID)

    override fun getPageKeyType(): String {
        return super.getPageKeyType() + "square"
    }
}