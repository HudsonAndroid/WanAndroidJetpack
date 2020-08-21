package com.hudson.wanandroid.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.Call
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.ProjectInfo
import com.hudson.wanandroid.data.entity.Projects
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.BaseNetworkBoundResource
import com.hudson.wanandroid.data.repository.paging.ProjectItemRemoteMediator
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/8/14.
 */
@Singleton
class ProjectsRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val wanAndroidApi: WanAndroidApi,
    private val db: WanAndroidDb
){
    fun loadProjectsCategory(): LiveData<Resource<List<ProjectInfo>>>{
        return object : BaseNetworkBoundResource<List<ProjectInfo>, Projects>(db.dataWrapperDao(),appExecutor){

            override fun shouldFetch(data: List<ProjectInfo>?): Boolean {
                return super.shouldFetch(data) || data == null || data.isEmpty()
            }

            override fun transform(requestType: Projects): List<ProjectInfo>? {
                return requestType.data
            }

            override fun createCall() = RetrofitCall(wanAndroidApi.projectsCategory())

        }.asLiveData()
    }

    fun loadProjectItemArticles(projectId: Int) = Pager(config = PagingConfig(pageSize = NETWORK_PAGE_SIZE),
        remoteMediator = ProjectItemRemoteMediator(wanAndroidApi, db, projectId)){
        db.articleDao().getProjectPagingSource(projectId)
    }.flow

    companion object{
        private const val NETWORK_PAGE_SIZE = 20
    }
}