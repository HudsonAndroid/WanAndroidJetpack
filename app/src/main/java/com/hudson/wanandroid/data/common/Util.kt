package com.hudson.wanandroid.data.common

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Response

/**
 * Created by Hudson on 2020/7/30.
 */
fun Response<*>.getErrorMsg(): String{
    if(!isSuccessful){
        val msg = errorBody()?.string()
        return if(msg.isNullOrEmpty()){
            message()
        }else{
            msg
        }
    }
    throw IllegalArgumentException("The response is successful, cannot fetch error msg")
}


fun <T> Gson.typedToJson(input: T): String = toJson(input)

// object : TypeToken<T>(){}.type
inline fun <reified T: Any> Gson.fromJson(json: String):T = fromJson(json, object : TypeToken<T>(){}.type)
// 注意不能使用如下方式，会出现集合被Gson转为LinkedTreeMap的问题，详见
// https://stackoverflow.com/questions/27253555/com-google-gson-internal-linkedtreemap-cannot-be-cast-to-my-class/27271365
// 以及测试用例 src/test/java/com/hudson/wanandroid/data/ConvertersTest.kt
//inline fun <reified T: Any> Gson.fromJson(json: String):T = fromJson(json, T::class.java)