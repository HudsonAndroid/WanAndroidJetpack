package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.Call
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.ProjectInfo
import com.hudson.wanandroid.data.entity.Projects
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.BaseNetworkBoundResource
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
}