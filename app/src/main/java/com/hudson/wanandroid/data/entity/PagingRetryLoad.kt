package com.hudson.wanandroid.data.entity

import androidx.paging.LoadState
import androidx.paging.LoadStates
import com.hudson.wanandroid.data.entity.wrapper.Status

/**
 * 只有网络数据获取失败之后，需要重试的paging加载使用
 * Created by Hudson on 2020/8/5.
 */
class PagingRetryLoad{
    var loadStates: LoadStates? = null
        set(value){
            judgeShowState(value)
            field = value
        }
    var status: Status = Status.SUCCESS
    var msg: Throwable? = null
    var canShowRetry = false
    var hasShowData = false
        set(value){
            canShowRetry = !field
            field = value
        }


    fun isEmptyPaging(): Boolean {
        return status == Status.SUCCESS && !hasShowData
    }

    private fun judgeShowState(loadStates: LoadStates?){
        loadStates?.run {
            // TODO: 2020/8/6 错误情况下，可以考虑显示网络请求错误toast
            if(refresh is LoadState.Error){
                status = Status.ERROR
                msg = (refresh as LoadState.Error).error
                canShowRetry = !hasShowData
                return
            }
            if(prepend is LoadState.Error){
                status = Status.ERROR
                msg = (prepend as LoadState.Error).error
                canShowRetry = !hasShowData
                return
            }
            if(append is LoadState.Error){
                status = Status.ERROR
                msg = (append as LoadState.Error).error
                canShowRetry = !hasShowData
                return
            }
            if(refresh is LoadState.Loading || prepend is LoadState.Loading ||
                    append is LoadState.Loading){
                canShowRetry = !hasShowData
                status = Status.LOADING
            }
            if(refresh is LoadState.NotLoading && prepend is LoadState.NotLoading &&
                    append is LoadState.NotLoading){
                status = Status.SUCCESS
                canShowRetry = false
            }
        }
    }
}