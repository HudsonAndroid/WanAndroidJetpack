package com.hudson.wanandroid.data.common.mergecall

import com.hudson.wanandroid.data.common.AppExecutor
import retrofit2.Response

/**
 * Created by Hudson on 2020/7/21.
 */
class MergeCall<Data1, Data2, T : MergeData<Data1,Data2>>(private val call1: Call<Data1>?,
                                                          private val call2: Call<Data2>?) : Call<T>{

    // todo 可以判断数据是否是BaseResult类型，以确认是否成功

    override fun execute(): Response<T> {
        commonCheck()
        val result: MergeData<Data1,Data2> = MergeData()
        var response1: Response<Data1>? = null
        val firstCallSuccess = if(call1 == null){
            true
        }else{
            response1 = call1.execute()
            result.data1 = response1.body()
            response1.isSuccessful
        }
        return if(firstCallSuccess){
            var response2: Response<Data2>? = null
            val secondCallSuccess = if(call2 == null){
                true
            }else{
                response2 = call2.execute()
                result.data2 = response2.body()
                response2.isSuccessful
            }
            if(secondCallSuccess){
                Response.success(result as T)
            }else{
                Response.error(response2!!.errorBody(), response2.raw())
            }
        }else{
            Response.error(response1!!.errorBody(), response1!!.raw())
        }
    }

    private fun commonCheck() {
        if (call1 == null && call2 == null) {
            throw IllegalArgumentException("merge call cannot both be null")
        }
    }

    override fun enqueue(callback: Callback<T>) {
        val appExecutor = AppExecutor.getInstance()
        appExecutor.networkExecutor.execute{
            val result: MergeData<Data1,Data2> = MergeData()
            var failure: Throwable? = null
            try{
                commonCheck()
                var response1: Response<Data1>? = null
                val firstCallSuccess = if(call1 == null){
                    true
                }else{
                    response1 = call1.execute()
                    result.data1 = response1.body()
                    response1.isSuccessful
                }
                if(firstCallSuccess){
                    var response2: Response<Data2>? = null
                    val secondCallSuccess = if(call2 == null){
                        true
                    }else{
                        response2 = call2.execute()
                        result.data2 = response2.body()
                        response2.isSuccessful
                    }
                    if(secondCallSuccess){
                        appExecutor.mainThreadExecutor.execute{
                            callback.onResponse(this, Response.success(result) as Response<T>)
                        }
                    }else{
                        failure = RuntimeException(getErrorMsg(response2!!))
                    }
                }else{
                    failure = RuntimeException(getErrorMsg(response1!!))
                }
            } catch (e: Exception){
                failure = e
            }
            if(failure != null){
                appExecutor.mainThreadExecutor.execute {
                    callback.onFailure(this, failure)
                }
            }
        }
    }

    private fun getErrorMsg(response: Response<*>): String{
        if(!response.isSuccessful){
            val msg = response.errorBody()?.string()
            return if(msg.isNullOrEmpty()){
                response.message()
            }else{
                msg
            }
        }
        throw IllegalArgumentException("the response is successful, cannot fetch error msg")
    }

}