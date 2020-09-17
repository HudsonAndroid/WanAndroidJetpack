package com.hudson.wanandroid.di

import android.app.Application
import com.hudson.wanandroid.WanAndroidApp
import com.hudson.wanandroid.di.module.*
import com.hudson.wanandroid.di.module.article.ArticleActivityModule
import com.hudson.wanandroid.di.module.login.LoginActivityModule
import com.hudson.wanandroid.di.module.main.MainActivityModule
import com.hudson.wanandroid.di.module.search.SearchActivityModule
import com.hudson.wanandroid.di.module.treelist.TreeListActivityModule
import com.hudson.wanandroid.di.module.userinfo.UserInfoActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/7/20.
 */
@Singleton //指明Scope为SingleTon
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class, //应用层级的
    MainActivityModule::class, //activity注入
    SearchActivityModule::class,
    TreeListActivityModule::class,
    LoginActivityModule::class,
    UserInfoActivityModule::class,
    ArticleActivityModule::class
])
interface AppComponent {

    fun inject(app: WanAndroidApp)

    @Component.Builder
    interface Builder{
        //对外隐藏Module实现，但需要依赖外界的Application（AppModule的provideWanAndroidDb需要依赖）
        @BindsInstance
        fun configApplication(application: Application): Builder

        fun build(): AppComponent
    }

}