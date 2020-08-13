package com.hudson.wanandroid.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hudson.wanandroid.data.entity.HotWord

/**
 * Created by Hudson on 2020/8/10.
 */
@Dao
interface HotWordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hotWord: HotWord)

    @Query("SELECT * FROM hotword")
    fun queryHistory(): LiveData<List<HotWord>>

    @Query("SELECT * FROM hotword")
    fun queryHistoryDirectly(): List<HotWord>

    @Query("DELETE FROM hotword")
    fun cleanHistory()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(hotWords: List<HotWord>)
}