package com.hudson.wanandroid.di.module

import com.hudson.wanandroid.ui.activity.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/8/29.
 */
@Suppress("unused")
@Module
abstract class LoginActivityModule {

    @ContributesAndroidInjector(modules = [FragmentsModule::class])
    abstract fun loginActivityInjector() : LoginActivity
}