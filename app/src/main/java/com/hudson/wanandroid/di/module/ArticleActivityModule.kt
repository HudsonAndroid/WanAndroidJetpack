package com.hudson.wanandroid.di.module

import com.hudson.wanandroid.ui.activity.ArticleActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/9/17.
 */
@Suppress("unused")
@Module
abstract class ArticleActivityModule{

    @ContributesAndroidInjector(modules = [ArticleFragmentsModule::class])
    abstract fun articleActivityInjector(): ArticleActivity
}