package com.hudson.wanandroid.di.module.setting

import com.hudson.wanandroid.ui.activity.SettingActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/9/25.
 */
@Suppress("unused")
@Module
abstract class SettingActivityModule {

    @ContributesAndroidInjector(modules = [SettingFragmentsModule::class])
    abstract fun settingActivityInjector(): SettingActivity
}