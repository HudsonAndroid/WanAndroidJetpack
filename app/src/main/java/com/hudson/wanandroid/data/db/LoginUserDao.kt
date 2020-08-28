package com.hudson.wanandroid.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hudson.wanandroid.data.entity.LoginUser

/**
 * Created by Hudson on 2020/8/28.
 */
@Dao
interface LoginUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(loginUser: LoginUser)

    @Query("SELECT * FROM loginuser")
    suspend fun allUsers(): List<LoginUser>

    @Query("SELECT * FROM loginuser WHERE id = :id")
    suspend fun getUser(id: Int): LoginUser

    @Query("SELECT * FROM loginuser WHERE current = :flag")
    fun getCurrentUser(flag: Boolean = true): LoginUser?
}