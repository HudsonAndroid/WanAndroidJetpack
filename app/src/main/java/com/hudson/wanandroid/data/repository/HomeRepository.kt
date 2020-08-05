package com.hudson.wanandroid.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.DataWrapperDao
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Banner
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.BaseNetworkBoundResource
import com.hudson.wanandroid.data.repository.paging.ArticleRemoteMediator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    fun loadBanners(): LiveData<Resource<List<BannerItem>>>{
        return object : BaseNetworkBoundResource<List<BannerItem>, Banner>(db.dataWrapperDao(), appExecutor){
            override fun shouldFetch(data: List<BannerItem>?): Boolean {
                return super.shouldFetch(data) || data == null || data.isEmpty()
            }

            override fun transform(requestType: Banner): List<BannerItem> {
                return requestType.data
            }

            override fun createCall() = RetrofitCall(wanAndroidApi.bannerApi())

        }.asLiveData()
    }

    fun loadArticles() = Pager(config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        remoteMediator = ArticleRemoteMediator(wanAndroidApi, db)){
        db.articleDao().getArticlePagingSource()
    }.flow
    
    companion object{
        private const val NETWORK_PAGE_SIZE = 20
    }
}