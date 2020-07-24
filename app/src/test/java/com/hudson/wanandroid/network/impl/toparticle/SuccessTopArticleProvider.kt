package com.hudson.wanandroid.network.impl.toparticle

import com.hudson.wanandroid.network.common.readFile
import com.hudson.wanandroid.network.interfaces.ITopArticleProvider
import okhttp3.*

/**
 * Created by Hudson on 2020/7/24.
 */
class SuccessTopArticleProvider : ITopArticleProvider{
    override fun provideTopArticle(chain: Interceptor.Chain): Response {
        val topArticleJsonStr = readFile("top-article.json")
        val body = ResponseBody.create(MediaType.parse("application/x-www-form-urlencoded"), topArticleJsonStr)
        return okhttp3.Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .body(body)
            .message("success")
            .build()
    }

}