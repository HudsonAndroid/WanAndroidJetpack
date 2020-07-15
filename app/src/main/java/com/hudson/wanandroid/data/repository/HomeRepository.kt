package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.DataWrapperDao
import com.hudson.wanandroid.data.entity.Banner
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.data.entity.wrapper.Resource

/**
 * Created by Hudson on 2020/7/14 0014.
 */

class HomeRepository(
    private val wanAndroidApi: WanAndroidApi,
    private val dataWrapperDao: DataWrapperDao
) {

    fun loadBanners(): LiveData<Resource<List<BannerItem>>>{
        return object : BaseNetworkBoundResource<List<BannerItem>, Banner>(dataWrapperDao){
            override fun shouldFetch(data: List<BannerItem>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun transform(requestType: Banner): List<BannerItem> {
                return requestType.data
            }

            override fun createCall() = wanAndroidApi.bannerApi()

        }.asLiveData()
    }
}