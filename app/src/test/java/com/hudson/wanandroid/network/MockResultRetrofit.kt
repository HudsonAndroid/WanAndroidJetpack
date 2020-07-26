package com.hudson.wanandroid.network

import com.hudson.wanandroid.network.common.SingleExecuteServiceAppExecutor
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

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

    private fun getOkhttpDispatcherForTest(): Dispatcher{
        return Dispatcher(SingleExecuteServiceAppExecutor.getInstance().ioExecutor as ExecutorService)
    }

    private fun getExecutorForTest(): Executor{
        return SingleExecuteServiceAppExecutor.getInstance().ioExecutor
    }

    fun <T> create(clazz: Class<T>): T{
        val okHttpClient = OkHttpClient.Builder()
            //这个是修改的Okhttp的dispatcher的线程池，而Retrofit对callback有一层回调回主线程的逻辑
                //所以，如果需要测试状态下能正常接收到Retrofit原始callback的回调，需要修改的是Retrofit的线程调度器
//            .dispatcher(getOkhttpDispatcherForTest())
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
                //修改Retrofit调度回主线程的调度器，以确保原始callback能够立刻回调
            .callbackExecutor(getExecutorForTest())
            .baseUrl("https://www.wanandroid.com")
            .build()
            .create(clazz)
    }
}