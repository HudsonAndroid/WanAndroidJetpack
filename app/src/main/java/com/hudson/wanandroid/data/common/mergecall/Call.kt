package com.hudson.wanandroid.data.common.mergecall

import retrofit2.Response

/**
 * Created by Hudson on 2020/7/21.
 */
interface Call<T>{
    fun enqueue(callback: Callback<T>)

    fun execute(): Response<T>
}