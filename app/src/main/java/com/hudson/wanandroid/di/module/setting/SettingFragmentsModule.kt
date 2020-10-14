package com.hudson.wanandroid.di.module.setting

import com.hudson.wanandroid.ui.fragment.setting.DefaultSettingFragment
import com.hudson.wanandroid.ui.fragment.setting.SearchSelectFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/9/25.
 */
@Suppress("unused")
@Module
abstract class SettingFragmentsModule {

    @ContributesAndroidInjector
    abstract fun defaultSettingFragmentInjector(): DefaultSettingFragment

    @ContributesAndroidInjector
    abstract fun searchSelectFragmentInjector(): SearchSelectFragment
}