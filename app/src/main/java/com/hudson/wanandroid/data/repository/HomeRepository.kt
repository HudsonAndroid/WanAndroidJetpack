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

    fun loadArticles() = Pager(PagingConfig(pageSize = 20),
        remoteMediator = ArticleRemoteMediator(wanAndroidApi, db, appExecutor)){
        Log.e("hudson","当前线程${Thread.currentThread().name}")
        db.articleDao().getArticlePagingSource()
    }.flow
//        .flowOn(Dispatchers.IO)
//        .onStart {
//            // 在调用flow请求数据前，做一些准备工作，例如显示正在加载数据的按钮
//        }
//        .catch {
//            // 捕获上游异常
//        }
//        .onCompletion {
//            //请求完成
//        }
//        .collectLatest {
//            // 请求完成
//        }
}