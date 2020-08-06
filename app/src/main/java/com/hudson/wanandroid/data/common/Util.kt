package com.hudson.wanandroid.data.common

import com.google.gson.Gson
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
inline fun <reified T: Any> Gson.fromJson(json: String):T = fromJson(json, T::class.java)