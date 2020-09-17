package com.hudson.wanandroid.di.module.main

import com.hudson.wanandroid.ui.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/7/21.
 */
@Suppress("unused")
@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    abstract fun mainActivityInjector() : MainActivity
}