package com.hudson.wanandroid.network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.repository.HomeAllRepository
import com.hudson.wanandroid.data.repository.HomeArticleRepository
import com.hudson.wanandroid.data.repository.HomeRepository
import com.hudson.wanandroid.network.common.SingleThreadAppExecutor
import com.hudson.wanandroid.network.common.TestHelp
import com.hudson.wanandroid.network.impl.banner.SuccessBannerProvider
import com.hudson.wanandroid.network.impl.homearticle.SuccessHomeArticleProvider
import com.hudson.wanandroid.network.impl.toparticle.EmptySuccessTopArticleProvider
import com.hudson.wanandroid.network.impl.toparticle.ErrorTopArticleProvider
import com.hudson.wanandroid.network.impl.toparticle.SuccessTopArticleProvider
import com.hudson.wanandroid.network.interfaces.IBannerProvider
import com.hudson.wanandroid.network.interfaces.IHomeArticleProvider
import com.hudson.wanandroid.network.interfaces.ITopArticleProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


/**
 * 踩坑记录：
 *     测试发现只会返回一个LOADING状态的结果，然后结束，以为是异步线程导致的，因此
 *     构建一个SingleThreadAppExecutor来使得单线程执行，发现依然如此。
 *     后面调试发现LiveData#postValue并没有通知到观察的对象上，经过查阅资料发现
 *     需要添加InstantTaskExecutorRule这个rule，来确保LiveData正常
 *
 * 额外注意：
 *      下面的rule必须添加@JvmField注解，以确保它是public的，否则会报异常
 * Created by Hudson on 2020/7/24.
 */
@RunWith(RobolectricTestRunner::class)
class NetworkTest {
    @Rule @JvmField
    var rule = InstantTaskExecutorRule()

    // 配置返回结果
    private fun responseConfig(bannerProvider: IBannerProvider = SuccessBannerProvider(),
                               homeArticleProvider: IHomeArticleProvider = SuccessHomeArticleProvider(),
                               topArticleProvider: ITopArticleProvider = SuccessTopArticleProvider()
                               ): ProviderConfig{
        return ProviderConfig.Builder()
            .bannerProvider(bannerProvider)
            .homeArticleProvider(homeArticleProvider)
            .topArticleProvider(topArticleProvider)
            .build()
    }

    @Test
    fun testHomeArticleRepository(){
        val appExecutor = SingleThreadAppExecutor.getInstance()
        val testHelp = TestHelp()
        val wanAndroidApi = MockResultRetrofit
            .configDataProvider(responseConfig(
//                homeArticleProvider = ErrorHomeArticleProvider() // 某一个请求出错测试
                topArticleProvider = ErrorTopArticleProvider()
            ))
            .create(WanAndroidApi::class.java)
        val dataWrapperDao =
            WanAndroidDb.getInstance(RuntimeEnvironment.application).dataWrapperDao()

//        val testActivity = Robolectric.setupActivity(TestActivity::class.java)
        // use observeForever without lifecycleOwner
        HomeArticleRepository(appExecutor, wanAndroidApi, dataWrapperDao)
            .loadArticles(0)
            .observeForever(Observer {
                println("fetch network state: $it")
                appExecutor.ioExecutor.execute{
                    testHelp.notifyCompleted(it.status)
                }
            })
//        subThread?.join()
        // 确保任务完成了才结束
        testHelp.latch.await()
    }

    @Test
    fun testApi(){
        val response = MockResultRetrofit
            .configDataProvider(responseConfig())
            .create(WanAndroidApi::class.java).topArticle().execute()
        println(response.body().toString())
    }

    @Test
    fun testRepository(){
        val appExecutor = SingleThreadAppExecutor.getInstance()
        val testHelp = TestHelp()
        val wanAndroidApi = MockResultRetrofit
            .configDataProvider(responseConfig())
            .create(WanAndroidApi::class.java)
        val dataWrapperDao =
            WanAndroidDb.getInstance(RuntimeEnvironment.application).dataWrapperDao()

        HomeRepository(appExecutor, wanAndroidApi, dataWrapperDao)
            .loadBanners()
            .observeForever(Observer {
                println("fetch network state: $it")
                appExecutor.ioExecutor.execute{
                    testHelp.notifyCompleted(it.status)
                }
            })
        // 确保任务完成了才结束
        testHelp.latch.await()
    }

    @Test
    fun testHomeAllRepository(){
        val appExecutor = SingleThreadAppExecutor.getInstance()
        val testHelp = TestHelp()
        val wanAndroidApi = MockResultRetrofit
            .configDataProvider(responseConfig(
                bannerProvider = SuccessBannerProvider(),// 成功
//                homeArticleProvider = ErrorHomeArticleProvider(), // 错误
                topArticleProvider = EmptySuccessTopArticleProvider() //空数据/204
            ))
            .create(WanAndroidApi::class.java)
        val dataWrapperDao =
            WanAndroidDb.getInstance(RuntimeEnvironment.application).dataWrapperDao()

        HomeAllRepository(appExecutor, wanAndroidApi, dataWrapperDao)
            .loadHomePageData(0)
            .observeForever(Observer {
                println("fetch network state: $it")
                appExecutor.ioExecutor.execute{
                    testHelp.notifyCompleted(it.status)
                }
            })
        // 确保任务完成了才结束
        testHelp.latch.await()
    }
}