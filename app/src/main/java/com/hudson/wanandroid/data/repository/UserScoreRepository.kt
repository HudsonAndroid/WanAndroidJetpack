package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.CurrentUserScore
import com.hudson.wanandroid.data.entity.UserScore
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.NetworkBoundResource
import com.hudson.wanandroid.data.repository.paging.ScoreRankRemoteMediator
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Hudson on 2020/9/1.
 */
@Singleton
class UserScoreRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val wanAndroidApi: WanAndroidApi,
    private val db: WanAndroidDb
){

    fun loadCurrentUserScore(userId: Long): LiveData<Resource<UserScore>>{
        return object: NetworkBoundResource<UserScore, CurrentUserScore>(true, appExecutor){
            override fun saveCallResult(item: CurrentUserScore) {
                db.userScoreDao().insertUserScore(item.data)
            }

            // we should use server data firstly
            override fun shouldFetch(data: UserScore?) = true

            override fun loadFromDb() = db.userScoreDao().getCurrentUserScore(userId)

            override fun createCall() = RetrofitCall(wanAndroidApi.fetchCurrentUserScore())

        }.asLiveData()
    }

    fun loadUserScoreRank() = Pager(config = PagingConfig(pageSize = LOAD_PAGE_SIZE),
        remoteMediator = ScoreRankRemoteMediator(wanAndroidApi, db)
    ){
        db.userScoreDao().getUserScoreRank(ScoreRankRemoteMediator.PAGING_TAG)
    }.flow

    companion object{
        const val LOAD_PAGE_SIZE = 20
    }
}