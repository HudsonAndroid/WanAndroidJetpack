package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Subject
import com.hudson.wanandroid.data.entity.SubjectWrapper
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.BaseNetworkBoundResource
import com.hudson.wanandroid.data.repository.base.NetworkBoundResource
import com.hudson.wanandroid.data.repository.paging.WechatRemoteMediator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/8/26.
 */
@Singleton
class WechatRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val wanAndroidApi: WanAndroidApi,
    private val db: WanAndroidDb
){
    companion object{
        private const val NETWORK_PAGE_SIZE = 20
    }
    private var resource: NetworkBoundResource<List<Subject>, SubjectWrapper>? = null

    fun loadWechatCategory(): LiveData<Resource<List<Subject>>>{
        resource = object : BaseNetworkBoundResource<List<Subject>, SubjectWrapper>(db.dataWrapperDao(), appExecutor){
            override fun shouldFetch(data: List<Subject>?): Boolean {
                return super.shouldFetch(data) || data == null || data.isEmpty()
            }

            override fun transform(requestType: SubjectWrapper) = requestType.data

            override fun createCall() = RetrofitCall(wanAndroidApi.wechatCategory())

            override fun identityInfo() = "home_tab_wechat"
        }
        return resource!!.asLiveData()
    }

    fun retry(){
        resource?.retry()
    }

    fun loadWechatItemArticles(wechatId: Int, superId: Int)
        = Pager(config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
            remoteMediator = WechatRemoteMediator(wanAndroidApi,db, wechatId, superId)){
        db.articleDao().getSubjectPagingSource(wechatId, superId)
    }.flow
}