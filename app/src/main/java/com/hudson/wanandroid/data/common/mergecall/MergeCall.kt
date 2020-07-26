package com.hudson.wanandroid.data.common.mergecall

import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.entity.BaseResult
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Response

/**
 * 规则：
 *      1）Call1和Call2可以其中一个为null，那么结果MergeData中
 *      对应的数据部分为null；但两者不能同时为null，否则没有意义
 *
 *      2）为了简便，Call1和Call2任意一个请求发生错误，都将触发失败
 *
 *      3）由于OkHttp的CacheInterceptor已经处理了304及其缓存的返回
 *      因此，只判断数据是否是空的情况，即204。如果两个Call中任意
 *      一个Call返回码为204或非空call返回空，那么对应的MergeCall中
 *      相应无新网络数据标识将会被置为true，同时整个Response的code
 *      被置为204，以便发起从ROOM中获取的请求，你需要在loadFromDb
 *      方法中根据标识决定是否重新从ROOM获取，并决定是否有新数据更新
 *      回数据库
 *
 * Created by Hudson on 2020/7/21.
 */
abstract class MergeCall<Data1, Data2, T : MergeData<Data1,Data2>>
        (private val call1: Call<Data1>?,
                            private val call2: Call<Data2>?,
                            private val appExecutor: AppExecutor = AppExecutor.getInstance()) : Call<T>{

    //这种方式也不行，Builder本身类没有子类，不晓得泛型具体类型，因此传递给MergeCall也就不知道了
//    open class Builder<Data1, Data2, T: MergeData<Data1,Data2>>{
//        private var call1: Call<Data1>? = null
//        private var call2: Call<Data2>? = null
//        private var appExecutor:AppExecutor = AppExecutor.getInstance()
//
//        fun call1(call: Call<Data1>?):Builder<Data1, Data2, T>{
//            call1 = call
//            return this
//        }
//
//        fun call2(call: Call<Data2>?):Builder<Data1, Data2, T>{
//            call2 = call
//            return this
//        }
//
//        fun appExecutor(appExecutor: AppExecutor): Builder<Data1, Data2, T>{
//            this.appExecutor = appExecutor
//            return this
//        }
//
//        fun build(): MergeCall<Data1,Data2,T>{
//            // you can use this type. see test folder generics to get more information
////            return object :MergeCall<TopArticle, HomeArticle, ArticleWrapper>(topArticleCall,
////                wrapperCall(
////                    wanAndroidApi.homeArticle(pageNo)
////                ),
////                appExecutor
////            ){}
//            return object: MergeCall<Data1,Data2,T>(call1,call2,appExecutor){}
//        }
//    }

    private fun judgeDataValid(call: Call<*>?, response: Response<*>?): Response<T>? {
        if(call != null && response != null){
            val tmp = response.body()
            if(tmp != null && tmp is BaseResult && !tmp.isSuccess()){
                return Response.error(ResponseBody
                    .create(MediaType.parse("application/x-www-form-urlencoded"),
                        tmp.errorMsg), response.raw())
            }
        }
        return null
    }

//    @Suppress("UNCHECKED_CAST")
//    private fun createTargetMergeDataInstance(): T{
//        val type = javaClass.genericSuperclass as ParameterizedType
//        println("${hashCode()} $javaClass ${type.actualTypeArguments[0]} ${type.actualTypeArguments[1]}  ${type.actualTypeArguments[2]}")
//        val mergeDataClass = type.actualTypeArguments[2] as Class<T>
//        val data1Class = type.actualTypeArguments[0] as Class<Data1>
//        val data2Class = type.actualTypeArguments[1] as Class<Data2>
//        try{
//            return mergeDataClass.getDeclaredConstructor(data1Class, data2Class)
//                .newInstance(null, null)
//        }catch (noSuchMethod:NoSuchMethodException){
//            throw NoSuchMethodException("MergeData in MergeCall should has a constructor with" +
//                    "$data1Class and $data2Class ParamTypes.")
//        }
//    }

    protected abstract fun createTargetMergeDataInstance(): T

    override fun execute(): Response<T> {
        commonCheck()
        val result: T = createTargetMergeDataInstance()
        var response1: Response<Data1>? = null
        val firstCallSuccess = if(call1 == null){
            true
        }else{
            response1 = call1.execute()
            result.data1 = response1.body()
            response1.isSuccessful
        }
        return if(firstCallSuccess){
            // 进一步判断call1数据的有效性
            val data1Valid = judgeDataValid(call1, response1)
            if(data1Valid != null){
                return data1Valid
            }
            var response2: Response<Data2>? = null
            val secondCallSuccess = if(call2 == null){
                true
            }else{
                response2 = call2.execute()
                result.data2 = response2.body()
                response2.isSuccessful
            }
            if(secondCallSuccess){
                // 进一步判断call2数据的有效性
                val data2Valid = judgeDataValid(call2, response2)
                if(data2Valid != null){
                    return data2Valid
                }
                // 判断数据空的情况
                return createSuccessResponse(response1, response2, result)
            }else{
                Response.error(response2!!.errorBody(), response2.raw())
            }
        }else{
            Response.error(response1!!.errorBody(), response1!!.raw())
        }
    }

    private fun createSuccessResponse(response1: Response<*>?, response2: Response<*>?,
                                      result: MergeData<*,*>): Response<T>{
        val data1Empty = response1 != null && (response1.code() == 204 || result.data1 == null)
        result.networkData1Empty = data1Empty
        val data2Empty = response2 != null && (response2.code() == 204 || result.data2 == null)
        result.networkData2Empty = data2Empty
        val code = if(data1Empty || data2Empty){
            204
        }else 200
        return Response.success(result as T, okhttp3.Response.Builder()
            .code(code)
            .message("OK")
            .protocol(Protocol.HTTP_1_1)
            .request(Request.Builder().url("http://localhost/").build())
            .build()
        )
    }

    private fun commonCheck() {
        if (call1 == null && call2 == null) {
            throw IllegalArgumentException("merge call cannot both be null")
        }
    }

    override fun enqueue(callback: Callback<T>) {
        appExecutor.networkExecutor.execute{
            val result: T = createTargetMergeDataInstance()
            var failure: Throwable? = null
            try{
                commonCheck()
                var response1: Response<Data1>? = null
                var firstCallSuccess = if(call1 == null){
                    true
                }else{
                    response1 = call1.execute()
                    result.data1 = response1.body()
                    response1.isSuccessful
                }
                // 进一步判断call1数据的有效性
                if(firstCallSuccess && call1 != null && response1 != null){
                    val tmp = result.data1
                    if(tmp != null && tmp is BaseResult && !tmp.isSuccess()){
                        failure = MergeCallException(collectErrorInfo(tmp.errorMsg, call1))
                        firstCallSuccess = false
                    }
                }
                if(firstCallSuccess){
                    var response2: Response<Data2>? = null
                    var secondCallSuccess = if(call2 == null){
                        true
                    }else{
                        response2 = call2.execute()
                        result.data2 = response2.body()
                        response2.isSuccessful
                    }
                    // 进一步判断Call2数据的有效性
                    if(secondCallSuccess && call2 != null && response2 != null){
                        val tmp = result.data2
                        if(tmp != null && tmp is BaseResult && !tmp.isSuccess()){
                            failure = MergeCallException(collectErrorInfo(tmp.errorMsg,call2))
                            secondCallSuccess = false
                        }
                    }
                    if(secondCallSuccess){
                        // 判断数据空的情况
                        val response = createSuccessResponse(response1, response2, result)
                        appExecutor.mainThreadExecutor.execute{
                            callback.onResponse(this, response)
                        }
                    }else{
                        failure = MergeCallException(
                            collectErrorInfo(getErrorMsg(response2!!),call2))
                    }
                }else{
                    failure = MergeCallException(
                        collectErrorInfo(getErrorMsg(response1!!), call1))
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

    private fun collectErrorInfo(msg: String, call: Call<*>?): String{
        return "error description: ${msg}, call type: $call"
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
        throw IllegalArgumentException("The response is successful, cannot fetch error msg")
    }

}