package com.hudson.wanandroid.network.common

import android.os.Handler
import android.os.Looper
import com.hudson.wanandroid.data.common.AppExecutor
import okhttp3.internal.Util
import java.util.concurrent.Executor
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 当时为了排查问题，创建的单线程测试线程池
 * 内容有修改，因此用处不大，可以忽略
 *
 * 2020.7.26
 * 失败回调会主线程时有用。如果直接使用主线程
 * Callback#onFailure不会回调回来，因此使用
 * 子线程配合InstantTaskExecutorRule完成回调
 * Created by Hudson on 2020/7/24.
 */
class SingleExecuteServiceAppExecutor : AppExecutor(
    Creator.getSingleExecutor(),
    Creator.getSingleExecutor(),
    Creator.getSingleExecutor()
//    MainThreadExecutor()
){

    companion object{
        @Volatile
        private var instance: SingleExecuteServiceAppExecutor? = null

        fun getInstance(): SingleExecuteServiceAppExecutor{
            return instance ?: synchronized(this){
                instance ?: SingleExecuteServiceAppExecutor().also {
                    instance = it
                }
            }
        }
    }

    // don't use this, otherwise, you will never receive the callback result
    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable?) {
            mainThreadHandler.post(command)
        }
    }
}

object Creator{
    private val executor = ThreadPoolExecutor(0, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
        SynchronousQueue<Runnable>(), Util.threadFactory("SingleExecuteServiceAppExecutor", false))
    fun getSingleExecutor() = executor
}
