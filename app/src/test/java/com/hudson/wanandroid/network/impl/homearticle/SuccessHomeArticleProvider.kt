package com.hudson.wanandroid.network.impl.homearticle

import com.hudson.wanandroid.network.common.readFile
import com.hudson.wanandroid.network.interfaces.IHomeArticleProvider
import okhttp3.*

/**
 * Created by Hudson on 2020/7/24.
 */
class SuccessHomeArticleProvider: IHomeArticleProvider{
    override fun provideHomeArticle(chain: Interceptor.Chain, pageNo: Int): Response {
        val homeArticleJsonStr = readFile("home-article-$pageNo.json")
        val body = ResponseBody.create(MediaType.parse("application/x-www-form-urlencoded"), homeArticleJsonStr)
        return okhttp3.Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .body(body)
            .message("success")
            .build()
    }

}