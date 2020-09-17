package com.hudson.wanandroid.di.module.main

import com.hudson.wanandroid.ui.fragment.login.LoginFragment
import com.hudson.wanandroid.ui.fragment.login.RegisterFragment
import com.hudson.wanandroid.ui.fragment.main.HomeFragment
import com.hudson.wanandroid.ui.fragment.main.ProjectsFragment
import com.hudson.wanandroid.ui.fragment.main.TreeFragment
import com.hudson.wanandroid.ui.fragment.main.WechatFragment
import com.hudson.wanandroid.ui.fragment.projects.ProjectItemFragment
import com.hudson.wanandroid.ui.fragment.tree.DefaultTreeFragment
import com.hudson.wanandroid.ui.fragment.tree.NavigateFragment
import com.hudson.wanandroid.ui.fragment.wechat.WechatItemFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Fragment注入Module
 * Created by Hudson on 2020/7/21.
 */
@Suppress("unused")
@Module
abstract class FragmentsModule {

    @ContributesAndroidInjector
    abstract fun homeFragmentInjector(): HomeFragment

    @ContributesAndroidInjector
    abstract fun wechatFragmentInjector(): WechatFragment

    @ContributesAndroidInjector
    abstract fun wechatItemFragmentInjector(): WechatItemFragment

    @ContributesAndroidInjector
    abstract fun projectFragmentInjector(): ProjectsFragment

    @ContributesAndroidInjector
    abstract fun treeFragmentInjector(): TreeFragment

    @ContributesAndroidInjector
    abstract fun projectItemFragmentInjector(): ProjectItemFragment

    @ContributesAndroidInjector
    abstract fun navigateFragmentInjector(): NavigateFragment

    @ContributesAndroidInjector
    abstract fun defaultTreeFragmentInjector(): DefaultTreeFragment

}