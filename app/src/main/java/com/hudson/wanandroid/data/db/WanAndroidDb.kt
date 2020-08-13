package com.hudson.wanandroid.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.HotWord
import com.hudson.wanandroid.data.entity.wrapper.DataWrapper

/**
 * Created by Hudson on 2020/7/14.
 */
@Database(
    entities = [
        DataWrapper::class,
        PagingNextKey::class,
        Article::class,
        HotWord::class
    ],
    version = 3,
    exportSchema = false
)
// DbMemberConverters被PagingNextKey使用
@TypeConverters(DbMemberConverters::class)
abstract class WanAndroidDb: RoomDatabase(){
    abstract fun dataWrapperDao(): DataWrapperDao
    abstract fun pagingNextKeyDao(): PagingNextKeyDao
    abstract fun articleDao(): ArticleDao
    abstract fun hotWordDao(): HotWordDao

    companion object{
        @Volatile
        private var instance: WanAndroidDb? = null

        fun getInstance(context: Context): WanAndroidDb{
            return instance?: synchronized(this){
                instance?: provideDb(context).also {
                    instance = it
                }
            }
        }

        private fun provideDb(context: Context): WanAndroidDb{
            return Room
                .databaseBuilder(context, WanAndroidDb::class.java, "wanandroid.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}