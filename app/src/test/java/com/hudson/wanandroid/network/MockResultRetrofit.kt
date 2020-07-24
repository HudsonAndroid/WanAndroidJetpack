package com.hudson.wanandroid.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Hudson on 2020/7/24.
 */
object MockResultRetrofit {

    var providerConfig: ProviderConfig = ProviderConfig.Builder().build()

    fun configDataProvider(providerConfig: ProviderConfig): MockResultRetrofit{
        this.providerConfig = providerConfig
        return this
    }

    private fun provideHomeArticle(chain: Interceptor.Chain,requestUrl: String) : Response{
        val replace = requestUrl.replace("/json", "")
        val pageNo = replace.substring(replace.length - 1).toInt()
        return providerConfig.homeArticleProvider.provideHomeArticle(chain,pageNo)
    }

    fun <T> create(clazz: Class<T>): T{
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor {
                val requestUrl = it.request().url().toString()
                println("request url is: $requestUrl")
                when{
                    requestUrl.endsWith("article/top/json") -> providerConfig.topArticleProvider.provideTopArticle(it)
                    requestUrl.contains("article/list") -> provideHomeArticle(it,requestUrl)
                    requestUrl.endsWith("/banner/json") -> providerConfig.bannerProvider.provideBanner(it)
                    else -> null
                }
            }.build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl("https://www.wanandroid.com")
            .build()
            .create(clazz)
    }
}