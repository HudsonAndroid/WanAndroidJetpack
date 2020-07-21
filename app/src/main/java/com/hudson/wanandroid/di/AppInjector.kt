package com.hudson.wanandroid.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.hudson.wanandroid.WanAndroidApp
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

/**
 * activity/fragment自动注入工具
 * Created by Hudson on 2020/7/17.
 */
object AppInjector {
    fun init(app: WanAndroidApp){
        DaggerAppComponent
            .builder()
            .configApplication(app)
            .build()
            .inject(app)
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
            override fun onActivityPaused(activity: Activity?) {
                
            }

            override fun onActivityResumed(activity: Activity?) {
                
            }

            override fun onActivityStarted(activity: Activity?) {
                
            }

            override fun onActivityDestroyed(activity: Activity?) {
                
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
                
            }

            override fun onActivityStopped(activity: Activity?) {
                
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                activity?.apply {
                    injectActivity(this)
                }
            }
        })
    }

    private fun injectActivity(activity: Activity){
        if(activity is HasSupportFragmentInjector){
            AndroidInjection.inject(activity)
        }
        if(activity is FragmentActivity){
            activity.supportFragmentManager
                .registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks(){
                    override fun onFragmentCreated(
                        fm: FragmentManager,
                        f: Fragment,
                        savedInstanceState: Bundle?
                    ) {
                        super.onFragmentCreated(fm, f, savedInstanceState)
                        if(f is Injectable){
                            AndroidSupportInjection.inject(f)
                        }
                    }
                }, true)
        }
    }
}