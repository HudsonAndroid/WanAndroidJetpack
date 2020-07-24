package com.hudson.wanandroid.network.impl.banner

import com.hudson.wanandroid.network.common.readFile
import com.hudson.wanandroid.network.interfaces.IBannerProvider
import okhttp3.*

/**
 * Created by Hudson on 2020/7/24.
 */
class SuccessBannerProvider : IBannerProvider{

    override fun provideBanner(chain: Interceptor.Chain): Response {
        val bannerJsonStr = readFile("banner.json")
        val body = ResponseBody.create(MediaType.parse("application/x-www-form-urlencoded"), bannerJsonStr)
        return okhttp3.Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .body(body)
            .message("success")
            .build()
    }

}