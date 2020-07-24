package com.hudson.wanandroid.network.interfaces

import okhttp3.Interceptor
import okhttp3.Response


/**
 * Created by Hudson on 2020/7/24.
 */
interface ITopArticleProvider {
    fun provideTopArticle(chain: Interceptor.Chain): Response
}