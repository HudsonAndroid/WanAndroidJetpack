package com.hudson.wanandroid.network.common

import okhttp3.*

/**
 * Created by Hudson on 2020/7/24.
 */
fun provideFailedData(chain: Interceptor.Chain): Response {
    return okhttp3.Response.Builder()
        .request(chain.request())
        .protocol(Protocol.HTTP_1_1)
        .code(404)
        .body(ResponseBody.create(MediaType.parse("application/x-www-form-urlencoded"), "not found"))
        .message("failed")
        .build()
}

fun provideEmptySuccessData(chain: Interceptor.Chain): Response {
    return okhttp3.Response.Builder()
        .request(chain.request())
        .protocol(Protocol.HTTP_1_1)
        .code(204)
            //不能传入null，success情况下，会去获取body的length，因此会导致空指针
        .body(ResponseBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""))
        .message("success")
        .build()
}
