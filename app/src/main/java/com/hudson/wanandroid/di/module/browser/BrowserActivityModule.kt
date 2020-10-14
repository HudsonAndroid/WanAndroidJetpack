package com.hudson.wanandroid.di.module.browser

import com.hudson.wanandroid.ui.activity.BrowserActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/9/18.
 */
@Suppress("unused")
@Module
abstract class BrowserActivityModule {

    @ContributesAndroidInjector(modules = [BrowserFragmentsModule::class])
    abstract fun browserActivityInjector(): BrowserActivity
}