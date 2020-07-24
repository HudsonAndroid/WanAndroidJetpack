package com.hudson.wanandroid.data.common

import android.os.Handler
import android.os.Looper
import okhttp3.internal.Util
import java.util.concurrent.*

/**
 * Created by Hudson on 2020/7/22.
 */
open class AppExecutor(
    val networkExecutor: Executor,
    val ioExecutor: Executor,
    val mainThreadExecutor: Executor
){

    constructor(): this(
        ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
        SynchronousQueue<Runnable>(), Util.threadFactory("AppExecutor", false)),
        Executors.newSingleThreadExecutor(),
        MainThreadExecutor()
    )

    private class MainThreadExecutor : Executor{
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable?) {
            mainThreadHandler.post(command)
        }
    }

    companion object{
        @Volatile
        private var instance: AppExecutor? = null

        fun getInstance(): AppExecutor{
            return instance ?: synchronized(this){
                instance ?: AppExecutor().also {
                    instance = it
                }
            }
        }
    }
}