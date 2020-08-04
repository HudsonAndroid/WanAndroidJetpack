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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by Hudson on 2020/7/30.
 */
class ArticleRemoteMediator(
    api: WanAndroidApi,
    db: WanAndroidDb
) : BaseRemoteMediator<Article>(api, db){
    private var nextKey: Int? = null

    override suspend fun fetchNetworkData(nextPageKey: Int?): List<Article> {
        nextPageKey?.run {
            val topArticleCall = if(nextPageKey == 0){
                wrapperCall(api.topArticle())
            }else{
                null
            }
            val call = object: MergeCall<TopArticle,HomeArticle,ArticleWrapper>
                (topArticleCall, wrapperCall(api.homeArticle(nextPageKey))){
                override fun createTargetMergeDataInstance(
                    data1: TopArticle?,
                    data2: HomeArticle?
                ) = ArticleWrapper(data1, data2)
            }
            // 由协程管理运行，不使用AppExecutor
            // 踩坑记录，在View层（Activity或Fragment中）将整个repository获取请求的操作利用
            // lifecycleScope或者其他协程管理执行时会出现偶发性的ChannelClosedException
            // 类似错误信息见 https://stackoverflow.com/questions/62633502/channel-was-closed-message-when-using-paging-3-from-androidx
//            val response = call.execute()
            val response = withContext(Dispatchers.IO){
                call.execute()
            }
            if(response.isSuccessful){
                val topArticle = response.body().data1
                val homeArticle = response.body().data2
                val result = mutableListOf<Article>()
                topArticle?.data?.apply {
                    result.addAll(this)
                }
                homeArticle?.data?.datas?.apply {
                    result.addAll(this)
                }
                nextKey = homeArticle?.data?.curPage?.run {
                    this /*+ 1*/  // 不需要+1，因为wanAndroid api中page和实际id差1
                }
                return result
            }else{
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

}