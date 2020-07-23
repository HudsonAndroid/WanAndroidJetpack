package com.hudson.wanandroid.data.common.mergecall

import retrofit2.Response

/**
 * Created by Hudson on 2020/7/22.
 */
interface Callback<T> {
    fun onResponse(call: Call<T>, response: Response<T>?)

    fun onFailure(call: Call<T>, t: Throwable?)
}