package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.db.DataWrapperDao
import com.hudson.wanandroid.data.entity.Banner
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.data.entity.wrapper.Resource
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
    val dataWrapperDao: DataWrapperDao
) {

    fun loadBanners(): LiveData<Resource<List<BannerItem>>>{
        return object : BaseNetworkBoundResource<List<BannerItem>, Banner>(dataWrapperDao, appExecutor){
            override fun shouldFetch(data: List<BannerItem>?): Boolean {
                return super.shouldFetch(data) || data == null || data.isEmpty()
            }

            override fun transform(requestType: Banner): List<BannerItem> {
                return requestType.data
            }

            override fun createCall() = wanAndroidApi.bannerApi()

        }.asLiveData()
    }
}