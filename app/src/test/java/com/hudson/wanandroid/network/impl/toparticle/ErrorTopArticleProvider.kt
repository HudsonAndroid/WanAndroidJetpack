package com.hudson.wanandroid.network.impl.toparticle

import com.hudson.wanandroid.network.common.provideFailedData
import com.hudson.wanandroid.network.interfaces.ITopArticleProvider
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Hudson on 2020/7/24.
 */
class ErrorTopArticleProvider : ITopArticleProvider{
    override fun provideTopArticle(chain: Interceptor.Chain): Response {
        return provideFailedData(chain)
    }

}