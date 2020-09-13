package com.hudson.wanandroid.di.module

import com.hudson.wanandroid.ui.activity.UserInfoActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/9/12.
 */
@Suppress("unused")
@Module
abstract class UserInfoActivityModule {

    @ContributesAndroidInjector(modules = [UserInfoFragmentsModule::class])
    abstract fun userInfoActivityInjector() : UserInfoActivity
}