package com.hudson.wanandroid.di.module

import com.hudson.wanandroid.ui.activity.AccountManageActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/9/1.
 */
@Suppress("unused")
@Module
abstract class AccountManageActivityModule {

    @ContributesAndroidInjector
    abstract fun accountManageActivityInjector(): AccountManageActivity
}