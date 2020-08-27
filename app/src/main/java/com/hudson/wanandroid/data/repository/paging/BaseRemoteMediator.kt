package com.hudson.wanandroid.data.repository.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.entity.PagingNextKey
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Article
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

/**
 * 第一步判断忽略，原因是如果优先判断本地
 * 数据是否失效，再决定从网络获取的话，
 * 各页数据需要单独管理其缓存的生命周期，
 * 因此有可能出现各页数据周期不相符合的
 * 问题。
 * Created by Hudson on 2020/7/29.
 */
@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator<T: Any> (
    val api: WanAndroidApi,
    val db: WanAndroidDb
): RemoteMediator<Int, T>(){
    override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
        try{
            // 1.判断是否需要从网络获取,如果不需要则直接使用本地数据
//            if(!shouldFetch()){
//                return MediatorResult.Success(endOfPaginationReached = true)
//            }
            // 2.获取从网络获取的数据的下标，相对于本地数据库而言是nextPageKey
            // 方案上：如果服务端返回的数据中包含了page信息的情况下，可以直接通过PagingState来获取上次
            // 请求的最后一页数据下标，并增加1得到；另一种更加通用的方案是利用PagingNextKey本地存储方式
            val pagingNextKeyDao = db.pagingNextKeyDao()
            var nextPageKey = when(loadType){
                // 如果是刷新或者首次获取，则从头开始，因此没有NextPageKey
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // 通过数据库缓存获取NextPageKey
                    val cacheKey = db.withTransaction {
                        pagingNextKeyDao.getNextKey(getPageKeyType())
                    }

                    if(cacheKey?.nextKey == null){
                        // 没有更多的数据了，因此通知到底了
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    cacheKey.nextKey
                }
            }
            nextPageKey = nextPageKey ?: getDefaultStartPageKey()
            // 3.使用拿到的nextPageKey获取该页数据
            val networkData = fetchNetworkData(nextPageKey)

            db.withTransaction {
                if(loadType == LoadType.REFRESH){
                    // 如果是刷新加载或首次访问，那么清空
                    pagingNextKeyDao.clearTargetKeyCache(getPageKeyType())
                    cleanLocalData(db)
                }
                pagingNextKeyDao.insert(
                    PagingNextKey(
                        getPageKeyType(),
                        getNextKey(nextPageKey)
                    )
                )
                updateNetworkData(db, networkData)
            }
            return MediatorResult.Success(endOfPaginationReached = networkData.isNullOrEmpty())
        }catch (e: IOException){
            e.printStackTrace()
            return MediatorResult.Error(e)
        }catch (e: HttpException){
            e.printStackTrace()
            return MediatorResult.Error(e)
        }catch (e: Exception){
            e.printStackTrace()
            return MediatorResult.Error(e)
        }
    }

    open fun getDefaultStartPageKey() = 0

    abstract suspend fun fetchNetworkData(nextPageKey: Int?): List<T>
    abstract fun getNextKey(nextPageKey: Int?): Int?
    // 注意对应的ROOM操作方法上需要添加suspend关键字，以确保不在主线程对ROOM操作
    abstract suspend fun updateNetworkData(db: WanAndroidDb, data: List<T>)
    abstract suspend fun cleanLocalData(db: WanAndroidDb)

//    // 默认每次都重新从网络上获取
//    open fun shouldFetch() = true

    protected open fun getPageKeyType(): String{
        return Article::class.java.name
    }

}