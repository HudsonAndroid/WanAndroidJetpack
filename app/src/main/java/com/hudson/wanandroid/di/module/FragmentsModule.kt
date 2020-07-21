package com.hudson.wanandroid.di.module

import com.hudson.wanandroid.ui.fragment.main.HomeFragment
import com.hudson.wanandroid.ui.fragment.main.NavigationFragment
import com.hudson.wanandroid.ui.fragment.main.ProjectsFragment
import com.hudson.wanandroid.ui.fragment.main.TreeFragment
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
    abstract fun navigationFragmentInjector(): NavigationFragment

    @ContributesAndroidInjector
    abstract fun projectFragmentInjector(): ProjectsFragment

    @ContributesAndroidInjector
    abstract fun treeFragmentInjector(): TreeFragment
}