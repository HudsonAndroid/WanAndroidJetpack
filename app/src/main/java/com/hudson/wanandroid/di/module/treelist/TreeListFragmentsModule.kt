package com.hudson.wanandroid.di.module.treelist

import com.hudson.wanandroid.ui.fragment.tree.TreeArticleFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/8/26.
 */
@Suppress("unused")
@Module
abstract class TreeListFragmentsModule {
    @ContributesAndroidInjector
    abstract fun treeArticleFragmentInjector(): TreeArticleFragment
}