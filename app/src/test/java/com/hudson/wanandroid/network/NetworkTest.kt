package com.hudson.wanandroid.network

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.repository.HomeAllRepository
import com.hudson.wanandroid.data.repository.HomeArticleRepository
import com.hudson.wanandroid.data.repository.HomeRepository
import com.hudson.wanandroid.network.common.SingleExecuteServiceAppExecutor
import com.hudson.wanandroid.network.common.TestHelp
import com.hudson.wanandroid.network.impl.banner.EmptySuccessBannerProvider
import com.hudson.wanandroid.network.impl.banner.SuccessBannerProvider
import com.hudson.wanandroid.network.impl.homearticle.SuccessHomeArticleProvider
import com.hudson.wanandroid.network.impl.toparticle.EmptySuccessTopArticleProvider
import com.hudson.wanandroid.network.impl.toparticle.SuccessTopArticleProvider
import com.hudson.wanandroid.network.interfaces.IBannerProvider
import com.hudson.wanandroid.network.interfaces.IHomeArticleProvider
import com.hudson.wanandroid.network.interfaces.ITopArticleProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


/**
 * 踩坑记录：
 *     测试发现只会返回一个LOADING状态的结果，然后结束，以为是异步线程导致的，因此
 *     构建一个SingleThreadAppExecutor来使得单线程执行，发现依然如此。
 *     后面调试发现LiveData#postValue并没有通知到观察的对象上，经过查阅资料发现
 *     需要添加InstantTaskExecutorRule这个rule，来确保LiveData正常
 *
 * 额外注意：
 *      1.下面的rule必须添加@JvmField注解，以确保它是public的，否则会报异常
 *      2.测试情况下，Repository使用的Executor必须使用测试状态下的线程调度器，
 *      否则会接收不到结果回调；除此之外，testBannerRepository属于特殊情况，
 *      具体看方法解释
 *
 * Created by Hudson on 2020/7/24.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
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
        val appExecutor = SingleExecuteServiceAppExecutor.getInstance()
        val testHelp = TestHelp()
        val wanAndroidApi = MockResultRetrofit
            .configDataProvider(responseConfig(
//                homeArticleProvider = ErrorHomeArticleProvider(), // 某一个请求出错测试
                topArticleProvider = SuccessTopArticleProvider()
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

    //这个使用了RetrofitCall，是对原始retrofit的封装，因此本质还是Retrofit的call，
    //由于retrofit对okhttp回调有一层包裹，使得回调会在主线程运行，导致下面测试方法
    //无法正常接收到回调，因此需要修改retrofit原始的callback的线程调度器来配合
    // InstantTaskExecutorRule，具体看MockResultRetrofit内部逻辑。
    // 其他方法由于受到我们自定义的Executor控制，因此不受影响
    @Test
    fun testBannerRepository(){
        val appExecutor = SingleExecuteServiceAppExecutor.getInstance()
        val testHelp = TestHelp()
        val wanAndroidApi = MockResultRetrofit
            .configDataProvider(responseConfig(
                bannerProvider = EmptySuccessBannerProvider()
            ))
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
        val appExecutor = SingleExecuteServiceAppExecutor.getInstance()
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