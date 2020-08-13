package com.hudson.wanandroid.data.repository.paging

import androidx.paging.PagingSource
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.entity.Article
import java.lang.Exception

/**
 * Created by Hudson on 2020/8/10.
 */
class SearchPagingSource(private val api: WanAndroidApi,
    private val searchWord: String) : PagingSource<Int, Article>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try{
            val pageKey = params.key ?: 0
            val data = api.searchHotResult(pageKey, searchWord).data
            LoadResult.Page(
                data = data.datas,
                //如果可以往上加载更多就设置该参数，否则不设置
                prevKey = null,
                //加载下一页的key，返回null表示没有更多数据
                nextKey = if(!data.over) data.curPage else null
            )
        }catch (e: Exception){
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

}