package com.hudson.wanandroid.data.common

import retrofit2.Response
import java.lang.RuntimeException

/**
 * Created by Hudson on 2020/8/5.
 */
class NiceHttpException(response: Response<*>?): RuntimeException(processResponse(response)){

    companion object{
        fun processResponse(response: Response<*>?): String{
            val msg = response?.run {
                val code = response.code()
                return when {
                    code >= 500 -> "Server Error"
                    code == 404 -> "Server Not Found Resource"
                    code == 403 -> "Server Forbidden Access"
                    code == 400 -> "Server Bad Request"
                    else -> response.getErrorMsg()
                }
            }
            return msg ?: "Unknown Exception"
        }
    }
}