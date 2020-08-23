package com.hudson.wanandroid.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.viewmodel.HomeModel
import com.hudson.wanandroid.viewmodel.provider.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * ViewModel的注入Module，协助注入
 *
 * 通过IntoMap构建出一个Map<Class, Provider<ViewModel>>的
 * map集合，再通过该集合构建出一个ViewModelProvider.Factory
 * 的工厂实例，所有ViewModel的构建都通过该工厂构建
 *
 * @Binds与@Provides类似，只不过前者不需要实现内容，后者需要
 * 提供具体实现；另外前者方法的参数必须是返回值的实现类
 * Created by Hudson on 2020/7/21.
 */
@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeModel::class)
    abstract fun bindBannerModel(homeModel: HomeModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}