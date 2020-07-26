package com.hudson.wanandroid.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hudson.wanandroid.data.entity.DataWrapper

/**
 * Created by Hudson on 2020/7/14.
 */
@Database(
    entities = [
        DataWrapper::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WanAndroidDb: RoomDatabase(){
    abstract fun dataWrapperDao(): DataWrapperDao

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