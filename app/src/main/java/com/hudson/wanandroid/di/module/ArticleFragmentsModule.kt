package com.hudson.wanandroid.di.module

import com.hudson.wanandroid.ui.fragment.other.AskArticleFragment
import com.hudson.wanandroid.ui.fragment.other.SquareArticleFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Hudson on 2020/9/17.
 */
@Suppress("unused")
@Module
abstract class ArticleFragmentsModule{

    @ContributesAndroidInjector
    abstract fun askFragmentInjector(): AskArticleFragment

    @ContributesAndroidInjector
    abstract fun squareFragmentInjector(): SquareArticleFragment
}