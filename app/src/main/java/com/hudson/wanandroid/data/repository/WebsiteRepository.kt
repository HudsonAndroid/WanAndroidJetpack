package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.Call
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.UserWebsite
import com.hudson.wanandroid.data.entity.UserWebsiteItem
import com.hudson.wanandroid.data.entity.wrapper.BaseResult
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.NetworkBoundResource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/9/18.
 */
@Singleton
class WebsiteRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val wanAndroidApi: WanAndroidApi,
    private val db: WanAndroidDb
){

    fun loadUserWebsite(userId: Long): LiveData<Resource<List<UserWebsiteItem>>> {
        return object : NetworkBoundResource<List<UserWebsiteItem>, UserWebsite>(appExecutor = appExecutor){
            override fun saveCallResult(item: UserWebsite) {
                db.userWebsiteDao().insertWebsites(item.data)
            }

            override fun shouldFetch(data: List<UserWebsiteItem>?) = true

            override fun loadFromDb(): LiveData<List<UserWebsiteItem>>  = db.userWebsiteDao().userWebsite(userId)

            override fun createCall(): Call<UserWebsite> {
                return RetrofitCall(wanAndroidApi.userWebsite())
            }
        }.asLiveData()
    }

    suspend fun starWebsite(name: String, link: String): BaseResult{
        val starWebsite = wanAndroidApi.starWebsite(name, link)
        if(starWebsite.isSuccess()){
            db.userWebsiteDao().insert(starWebsite.data)
        }
        return starWebsite
    }

    suspend fun checkUrlStared(url: String): Boolean{
        return db.userWebsiteDao().queryWebsite(url) != null
    }
}