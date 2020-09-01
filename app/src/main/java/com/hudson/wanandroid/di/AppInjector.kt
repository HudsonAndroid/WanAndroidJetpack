package com.hudson.wanandroid.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.collection.ArrayMap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.hudson.wanandroid.WanAndroidApp
import com.hudson.wanandroid.data.account.AccountRelative
import com.hudson.wanandroid.data.account.WanAndroidAccount
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber

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
                    accountRelateActivity(this)
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

    private fun accountRelateActivity(activity: Activity){
        // 协助完成账号变动监听
        val userChangeLiveData = WanAndroidAccount.getInstance().currentUser
        if(activity is AccountRelative && activity is LifecycleOwner){
            userChangeLiveData.observe(activity, Observer { activity.onAccountChanged(it) })
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
                        if(f is AccountRelative){
                            userChangeLiveData.observe(f, Observer { f.onAccountChanged(it) })
                        }
                    }
                }, true)
        }
    }
}