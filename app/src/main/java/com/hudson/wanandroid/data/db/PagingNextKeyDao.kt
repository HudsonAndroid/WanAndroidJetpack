package com.hudson.wanandroid.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Hudson on 2020/7/29.
 */
@Dao
interface PagingNextKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pagingNextKey: PagingNextKey)

    @Query("SELECT * FROM PagingNextKey where type = :type")
    suspend fun getNextKey(type: Class<*>): PagingNextKey?

    @Query("DELETE FROM PagingNextKey where type = :type")
    suspend fun clearTargetKeyCache(type: Class<*>)
}