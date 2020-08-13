package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.Call
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.HotWord
import com.hudson.wanandroid.data.entity.SearchHotWord
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.BaseNetworkBoundResource
import com.hudson.wanandroid.data.repository.paging.SearchPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/8/10.
 */
@Singleton
class HotSearchWordRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val wanAndroidApi: WanAndroidApi,
    private val db: WanAndroidDb
){

    fun loadHotSearchWord(): LiveData<Resource<List<HotWord>>>{
        return object : BaseNetworkBoundResource<List<HotWord>, SearchHotWord>(db.dataWrapperDao(),appExecutor){

            override fun shouldFetch(data: List<HotWord>?): Boolean {
                return super.shouldFetch(data) || data == null || data.isEmpty()
            }

            override fun transform(requestType: SearchHotWord): List<HotWord>? {
                return requestType.data
            }

            override fun createCall(): Call<SearchHotWord> {
                return RetrofitCall(wanAndroidApi.searchHotWordApi())
            }

        }.asLiveData()
    }

    fun loadHistorySearch() = db.hotWordDao().queryHistory()

    suspend fun storeHistorySearch(hotWord: HotWord) = db.hotWordDao().insert(hotWord)

    suspend fun cleanHistorySearch() {
        withContext(Dispatchers.IO){
            db.hotWordDao().cleanHistory()
        }
    }

    fun fetchSearchResult(searchWord: String) = Pager(PagingConfig(pageSize = PAGE_SIZE)){
        SearchPagingSource(wanAndroidApi, searchWord)
    }.flow

    companion object{
        const val PAGE_SIZE = 20
    }
}