package com.hudson.wanandroid.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hudson.wanandroid.BuildConfig
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.HotWord
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.PagingNextKey
import com.hudson.wanandroid.data.entity.wrapper.DataWrapper
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

/**
 * Created by Hudson on 2020/7/14.
 */
@Database(
    entities = [
        DataWrapper::class,
        PagingNextKey::class,
        Article::class,
        HotWord::class,
        LoginUser::class
    ],
    version = 4,
    exportSchema = false
)
// DbMemberConverters被PagingNextKey使用
@TypeConverters(DbMemberConverters::class)
abstract class WanAndroidDb: RoomDatabase(){
    abstract fun dataWrapperDao(): DataWrapperDao
    abstract fun pagingNextKeyDao(): PagingNextKeyDao
    abstract fun articleDao(): ArticleDao
    abstract fun hotWordDao(): HotWordDao
    abstract fun loginUserDao(): LoginUserDao

    companion object{
        @Volatile
        private var instance: WanAndroidDb? = null
        private const val PASSWORD = "hudson_jetpack_particle"

        fun getInstance(context: Context): WanAndroidDb{
            return instance?: synchronized(this){
                instance?: provideDb(context).also {
                    instance = it
                }
            }
        }

        private fun provideDb(context: Context): WanAndroidDb{
            val dbName = "wanandroid.db"
            return if(BuildConfig.DEBUG){
                Room
                    .databaseBuilder(context, WanAndroidDb::class.java, dbName)
                    .fallbackToDestructiveMigration()
                    .build()
            }else{
                // 如果是正式环境下，加密数据库
                // see https://github.com/sqlcipher/android-database-sqlcipher
                val passphrase = SQLiteDatabase.getBytes(PASSWORD.toCharArray())
                Room.databaseBuilder(context, WanAndroidDb::class.java, dbName)
                    .openHelperFactory(SupportFactory(passphrase))
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}