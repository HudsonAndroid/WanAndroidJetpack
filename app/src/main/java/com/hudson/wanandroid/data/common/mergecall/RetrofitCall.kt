package com.hudson.wanandroid.data.common.mergecall

import retrofit2.Response

/**
 * Created by Hudson on 2020/7/21.
 */
class RetrofitCall<T>(private val call: retrofit2.Call<T>): Call<T>{
    override fun execute(): Response<T> {
        return call.execute()
    }

    override fun enqueue(callback: Callback<T>) {
        call.enqueue(object : retrofit2.Callback<T>{
            override fun onFailure(call: retrofit2.Call<T>?, t: Throwable?) {
                callback.onFailure(this@RetrofitCall, t)
            }

            override fun onResponse(call: retrofit2.Call<T>?, response: Response<T>?) {
                callback.onResponse(this@RetrofitCall, response)
            }
        })
    }
}