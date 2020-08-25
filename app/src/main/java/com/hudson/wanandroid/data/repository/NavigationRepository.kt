package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.Call
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.NavigationItem
import com.hudson.wanandroid.data.entity.NavigationWrapper
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.BaseNetworkBoundResource
import com.hudson.wanandroid.data.repository.base.NetworkBoundResource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/8/25.
 */
@Singleton
class NavigationRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val wanAndroidApi: WanAndroidApi,
    private val db: WanAndroidDb
){
    private var resource: NetworkBoundResource<List<NavigationItem>, NavigationWrapper>? = null

    fun loadNavigation(): LiveData<Resource<List<NavigationItem>>>{
        resource = object :
            BaseNetworkBoundResource<List<NavigationItem>, NavigationWrapper>(db.dataWrapperDao(), appExecutor) {
            override fun shouldFetch(data: List<NavigationItem>?): Boolean {
                return super.shouldFetch(data) || data == null || data.isEmpty()
            }

            override fun transform(requestType: NavigationWrapper) = requestType.data

            override fun createCall() = RetrofitCall(wanAndroidApi.navigation())
        }
        return resource!!.asLiveData()
    }

    fun retry(){
        resource?.retry()
    }
}