package com.hudson.wanandroid.di.module.search

import com.hudson.wanandroid.ui.fragment.search.SearchDefaultFragment
import com.hudson.wanandroid.ui.fragment.search.SearchResultFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/8/10.
 */
@Suppress("unused")
@Module
abstract class SearchFragmentsModule {
    @ContributesAndroidInjector
    abstract fun searchDefaultFragmentInjector(): SearchDefaultFragment

    @ContributesAndroidInjector
    abstract fun searchResultFragmentInjector(): SearchResultFragment
}