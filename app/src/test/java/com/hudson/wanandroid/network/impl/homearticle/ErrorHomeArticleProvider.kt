package com.hudson.wanandroid.network.impl.homearticle

import com.hudson.wanandroid.network.common.provideFailedData
import com.hudson.wanandroid.network.interfaces.IHomeArticleProvider
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Hudson on 2020/7/24.
 */
class ErrorHomeArticleProvider : IHomeArticleProvider{
    override fun provideHomeArticle(chain: Interceptor.Chain, pageNo: Int): Response {
        return provideFailedData(chain)
    }

}