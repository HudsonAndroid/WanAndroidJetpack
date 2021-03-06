package com.hudson.wanandroid.network.mergecall.repository

import androidx.lifecycle.LiveData
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.Call
import com.hudson.wanandroid.data.common.mergecall.MergeCall
import com.hudson.wanandroid.data.db.DataWrapperDao
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.ArticleResultWrapper
import com.hudson.wanandroid.data.entity.TopArticle
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.BaseSimpleMergeDataSource
import com.hudson.wanandroid.data.repository.base.convertTypeOrNull
import com.hudson.wanandroid.data.repository.base.wrapperCall
import com.hudson.wanandroid.network.mergecall.entity.ArticleWrapper
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/7/22.
 */
@Singleton
class ArticleRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val wanAndroidApi: WanAndroidApi,
    private val dataWrapperDao: DataWrapperDao
){

    fun loadArticles(pageNo: Int): LiveData<Resource<List<Article>>>{
        return object : BaseSimpleMergeDataSource<List<Article>, ArticleWrapper>(dataWrapperDao, appExecutor){

            override fun createTargetMergeDataInstance(data1: Any?, data2: Any?): ArticleWrapper {
                return ArticleWrapper(convertTypeOrNull(data1), convertTypeOrNull(data2))
            }

            override fun shouldFetch(data: List<Article>?): Boolean {
                return super.shouldFetch(data) || data == null || data.isEmpty()
            }

            override fun transform(requestType: ArticleWrapper): List<Article>? {
                val topArticle = requestType.data1
                val homeArticle = requestType.data2
                val result = mutableListOf<Article>()
                topArticle?.data?.apply {
                    result.addAll(this)
                }
                homeArticle?.data?.datas?.apply {
                    result.addAll(this)
                }
                return result
            }

            override fun createCall(): Call<ArticleWrapper> {
                // 如果请求第一页数据，那么把置顶文章合并带上
                val topArticleCall = if(pageNo == 0){
                    wrapperCall(wanAndroidApi.topArticle())
                }else{
                    null
                }
                return object: MergeCall<TopArticle, ArticleResultWrapper, ArticleWrapper>(topArticleCall,
                    wrapperCall(wanAndroidApi.homeArticle(pageNo)),appExecutor){
                    override fun createTargetMergeDataInstance(
                        data1: TopArticle?,
                        data2: ArticleResultWrapper?
                    ): ArticleWrapper {
                        return ArticleWrapper(data1, data2)
                    }
                }
            }

            override fun firstIdentityInfo(): String {
                return pageNo.toString()
            }

            override fun secondIdentityInfo(): String {
                return pageNo.toString()
            }

        }.asLiveData()
    }
}