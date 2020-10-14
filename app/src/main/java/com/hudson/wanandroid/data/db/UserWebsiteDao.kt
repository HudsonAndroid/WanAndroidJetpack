package com.hudson.wanandroid.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hudson.wanandroid.data.entity.UserWebsiteItem

/**
 * Created by Hudson on 2020/9/18.
 */
@Dao
interface UserWebsiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userWebsite: UserWebsiteItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWebsites(websites: List<UserWebsiteItem>)

    @Query("SELECT * FROM userwebsiteitem WHERE userId = :userId")
    fun userWebsite(userId: Long): LiveData<List<UserWebsiteItem>>

    @Update
    suspend fun updateWebsite(userWebsite: UserWebsiteItem)

    @Query("DELETE FROM userwebsiteitem WHERE id =:id AND userId = :userId")
    suspend fun removeWebsite(id: Int, userId: Int)

    @Query("SELECT * FROM userwebsiteitem WHERE link= :url")
    suspend fun queryWebsite(url: String): UserWebsiteItem?
}