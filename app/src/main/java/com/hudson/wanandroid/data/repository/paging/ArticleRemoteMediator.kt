package com.hudson.wanandroid.data.repository.paging

import android.util.Log
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.getErrorMsg
import com.hudson.wanandroid.data.common.mergecall.MergeCall
import com.hudson.wanandroid.data.common.mergecall.MergeCallException
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.ArticleWrapper
import com.hudson.wanandroid.data.entity.HomeArticle
import com.hudson.wanandroid.data.entity.TopArticle
import com.hudson.wanandroid.data.repository.base.wrapperCall

/**
 * Created by Hudson on 2020/7/30.
 */
class ArticleRemoteMediator(
    api: WanAndroidApi,
    db: WanAndroidDb,
    val appExecutor: AppExecutor
) : BaseRemoteMediator<Article>(api, db){
    private var nextKey: Int? = null

    override suspend fun fetchNetworkData(nextPageKey: Int?): List<Article> {
        nextPageKey?.run {
            Log.e("hudson","开始配置${Thread.currentThread().name}")
            val topArticleCall = if(nextPageKey == 0){
                wrapperCall(api.topArticle())
            }else{
                null
            }
            val call = object: MergeCall<TopArticle,HomeArticle,ArticleWrapper>
                (topArticleCall, wrapperCall(api.homeArticle(nextPageKey)), appExecutor){
                override fun createTargetMergeDataInstance(
                    data1: TopArticle?,
                    data2: HomeArticle?
                ) = ArticleWrapper(data1, data2)
            }
            Log.e("hudson","完成构建")
            val response = call.execute()
            Log.e("hudson","响应$response")
            if(response.isSuccessful){
                val topArticle = response.body().data1
                val homeArticle = response.body().data2
                val result = mutableListOf<Article>()
                topArticle?.data?.apply {
                    result.addAll(this)
                }
                Log.e("hudson","置顶文章${result}")
                homeArticle?.data?.datas?.apply {
                    result.addAll(this)
                }
                nextKey = homeArticle?.data?.curPage?.run {
                    this /*+ 1*/  // 不需要+1，因为wanandroid api中page和实际id差1
                }
                Log.e("hudson","数据大小${result}")
                return result
            }else{
                Log.e("hudson","失败")
                throw MergeCallException(response.getErrorMsg())
            }
        }
        return mutableListOf()
    }

    override fun getNextKey(nextPageKey: Int?): Int? {
        return nextKey
    }

    override suspend fun updateNetworkData(db: WanAndroidDb, data: List<Article>) {
        db.articleDao().insertArticles(data)
    }

    override suspend fun cleanLocalData(db: WanAndroidDb) {
        db.articleDao().cleanArticles()
    }

    override fun shouldFetch() = true

}