package com.hudson.wanandroid.di.module.browser

import com.hudson.wanandroid.ui.fragment.browser.BrowserDefaultFragment
import com.hudson.wanandroid.ui.fragment.browser.BrowserMainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/9/18.
 */
@Suppress("unused")
@Module
abstract class BrowserFragmentsModule {

    @ContributesAndroidInjector
    abstract fun browserDefaultFragmentInjector(): BrowserDefaultFragment

    @ContributesAndroidInjector
    abstract fun browserMainFragmentInjector(): BrowserMainFragment
}