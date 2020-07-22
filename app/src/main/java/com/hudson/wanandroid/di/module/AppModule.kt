package com.hudson.wanandroid.di.module

import android.app.Application
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.db.DataWrapperDao
import com.hudson.wanandroid.data.db.WanAndroidDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/7/20.
 */
//includes表示外界引入AppModule时，将会同时引入ViewModelModule
@Module(includes = [ViewModelModule::class])
class AppModule {

    @Provides
    @Singleton
    fun provideWanAndroidApi(): WanAndroidApi {
        return WanAndroidApi.api()
    }

    //该方法将自动使用provideWanAndroidDb的结果作为参数
    @Provides
    @Singleton
    fun provideDataWrapperDao(wanAndroidDb: WanAndroidDb): DataWrapperDao{
        return wanAndroidDb.dataWrapperDao()
    }

    @Provides
    @Singleton
    fun provideWanAndroidDb(context: Application): WanAndroidDb{
        return WanAndroidDb.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideAppExecutor(): AppExecutor{
        return AppExecutor.getInstance()
    }
}