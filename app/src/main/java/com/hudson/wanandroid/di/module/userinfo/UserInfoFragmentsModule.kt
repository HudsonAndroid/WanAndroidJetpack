package com.hudson.wanandroid.di.module.userinfo

import com.hudson.wanandroid.ui.fragment.userinfo.UserScoreFragment
import com.hudson.wanandroid.ui.fragment.userinfo.UserStarFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/9/12.
 */
@Suppress("unused")
@Module
abstract class UserInfoFragmentsModule {
    @ContributesAndroidInjector
    abstract fun userStarFragmentInjector(): UserStarFragment

    @ContributesAndroidInjector
    abstract fun userScoreFragmentInjector(): UserScoreFragment
}