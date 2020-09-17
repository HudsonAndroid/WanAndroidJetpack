package com.hudson.wanandroid.di.module.login

import com.hudson.wanandroid.ui.fragment.login.LoginFragment
import com.hudson.wanandroid.ui.fragment.login.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/9/17.
 */
@Suppress("unused")
@Module
abstract class LoginFragmentsModule {

    @ContributesAndroidInjector
    abstract fun loginFragmentInjector(): LoginFragment

    @ContributesAndroidInjector
    abstract fun registerFragmentInjector(): RegisterFragment
}