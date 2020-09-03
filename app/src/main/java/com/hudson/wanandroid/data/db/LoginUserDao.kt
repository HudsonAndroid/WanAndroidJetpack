package com.hudson.wanandroid.data.db

import androidx.lifecycle.LiveData
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(loginUsers: List<LoginUser>)

    // LiveData返回类型不能与suspend共用
    @Query("SELECT * FROM loginuser")
    fun allUsers(): LiveData<List<LoginUser>>

    @Query("SELECT * FROM loginuser WHERE id = :id")
    suspend fun getUser(id: Long): LoginUser

    @Query("SELECT * FROM loginuser WHERE current = :flag")
    fun getCurrentUser(flag: Boolean = true): LoginUser?

    @Query("DELETE FROM loginuser WHERE current = :flag")
    suspend fun removeCurrentUser(flag: Boolean = true)
}