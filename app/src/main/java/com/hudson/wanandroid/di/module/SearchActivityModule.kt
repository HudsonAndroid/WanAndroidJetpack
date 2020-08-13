package com.hudson.wanandroid.di.module

import com.hudson.wanandroid.ui.activity.SearchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/8/10.
 */
@Suppress("unused")
@Module
abstract class SearchActivityModule {

    @ContributesAndroidInjector(modules = [SearchFragmentsModule::class])
    abstract fun searchActivityInjector(): SearchActivity
}