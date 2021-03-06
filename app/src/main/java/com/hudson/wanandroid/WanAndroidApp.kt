package com.hudson.wanandroid

import android.app.Activity
import android.app.Application
import android.content.Context
import com.hudson.wanandroid.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Hudson on 2020/7/17.
 */
class WanAndroidApp : Application(), HasActivityInjector{
    // dagger-android,内部维护了一个Map集合，Activity是依赖这个来注入的
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        // config timber
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }
        GLOBAL_CONTEXT = this
        AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector

    companion object{
        var GLOBAL_CONTEXT: Context? = null
    }
}