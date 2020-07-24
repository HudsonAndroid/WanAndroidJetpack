package com.hudson.wanandroid.network.common

import com.hudson.wanandroid.data.common.AppExecutor
import okhttp3.internal.Util
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 当时为了排查问题，创建的单线程测试线程池
 * 内容有修改，因此用处不大，可以忽略
 * Created by Hudson on 2020/7/24.
 */
class SingleThreadAppExecutor : AppExecutor(
    Creator.getSingleThreadExecutor(),
    Creator.getSingleThreadExecutor(),
    Creator.getSingleThreadExecutor()
){

    companion object{
        @Volatile
        private var instance: SingleThreadAppExecutor? = null

        fun getInstance(): SingleThreadAppExecutor{
            return instance ?: synchronized(this){
                instance ?: SingleThreadAppExecutor().also {
                    instance = it
                }
            }
        }
    }
}

object Creator{
    private val executor = ThreadPoolExecutor(1, Int.MAX_VALUE, 60, TimeUnit.SECONDS,
        SynchronousQueue<Runnable>(), Util.threadFactory("SingleThreadAppExecutor", false))
    fun getSingleThreadExecutor() = executor
}
