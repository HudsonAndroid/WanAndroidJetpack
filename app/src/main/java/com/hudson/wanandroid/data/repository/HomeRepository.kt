package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.Banner
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.BaseNetworkBoundResource
import com.hudson.wanandroid.data.repository.base.NetworkBoundResource
import com.hudson.wanandroid.data.repository.paging.ArticleRemoteMediator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 向外界提供注入
 * Created by Hudson on 2020/7/14.
 */
@Singleton
class HomeRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val wanAndroidApi: WanAndroidApi,
    private val db: WanAndroidDb
) {
    private var resource: NetworkBoundResource<List<BannerItem>, Banner>? = null

    fun loadBanners(): LiveData<Resource<List<BannerItem>>>{
        resource = object :
            BaseNetworkBoundResource<List<BannerItem>, Banner>(db.dataWrapperDao(), appExecutor) {
            override fun shouldFetch(data: List<BannerItem>?): Boolean {
                return super.shouldFetch(data) || data == null || data.isEmpty()
            }

            override fun transform(requestType: Banner): List<BannerItem> {
                return requestType.data
            }

            override fun createCall() = RetrofitCall(wanAndroidApi.bannerApi())

        }
        return resource!!.asLiveData()
    }

    fun retry(){
        resource?.retry()
    }

    fun loadArticles() = Pager(config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        remoteMediator = ArticleRemoteMediator(wanAndroidApi, db)){
        db.articleDao().getArticlePagingSource()
    }.flow

    suspend fun starArticle(article: Article){
        val result = wanAndroidApi.starArticle(article.id)
        if(result.isSuccess()){
            article.collect = true
            // we should update local star database
            db.articleDao().insertArticle(article)
        }
    }
    
    companion object{
        private const val NETWORK_PAGE_SIZE = 20
    }
}