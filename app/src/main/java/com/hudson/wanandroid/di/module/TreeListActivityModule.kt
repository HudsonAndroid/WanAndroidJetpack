package com.hudson.wanandroid.di.module

import com.hudson.wanandroid.ui.activity.TreeListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/8/26.
 */
@Suppress("unused")
@Module
abstract class TreeListActivityModule {

    @ContributesAndroidInjector(modules = [TreeListFragmentsModule::class])
    abstract fun treeListActivityInjector(): TreeListActivity
}