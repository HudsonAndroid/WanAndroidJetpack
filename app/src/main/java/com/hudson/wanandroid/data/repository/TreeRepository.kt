package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Subject
import com.hudson.wanandroid.data.entity.TreeWrapper
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.BaseNetworkBoundResource
import com.hudson.wanandroid.data.repository.base.NetworkBoundResource
import com.hudson.wanandroid.data.repository.paging.TreeItemRemoteMediator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/8/24.
 */
@Singleton
class TreeRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val wanAndroidApi: WanAndroidApi,
    private val db:WanAndroidDb
){
    private var resource: NetworkBoundResource<List<Subject>, TreeWrapper>? = null

    fun loadTree(): LiveData<Resource<List<Subject>>>{
        resource = object :
            BaseNetworkBoundResource<List<Subject>, TreeWrapper>(db.dataWrapperDao(), appExecutor) {
            override fun shouldFetch(data: List<Subject>?): Boolean {
                return super.shouldFetch(data) || data == null || data.isEmpty()
            }

            override fun transform(requestType: TreeWrapper): List<Subject>? {
                return requestType.data
            }

            override fun createCall() = RetrofitCall(wanAndroidApi.treeCategory())

        }
        return resource!!.asLiveData()
    }

    fun retry(){
        resource?.retry()
    }

    fun loadTreeItemArticles(treeId: Int, superId: Int)
            = Pager(config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        remoteMediator = TreeItemRemoteMediator(wanAndroidApi, db, treeId, superId)){
        db.articleDao().getTreePagingSource(treeId, superId)
    }.flow

    companion object{
        private const val NETWORK_PAGE_SIZE = 20
    }
}