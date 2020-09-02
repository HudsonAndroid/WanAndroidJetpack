package com.hudson.wanandroid.data.db

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hudson.wanandroid.data.entity.UserScore

/**
 * Created by Hudson on 2020/9/1.
 */
@Dao
interface UserScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserScore(userScore: UserScore)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserScoreList(userScoreList: List<UserScore>)

    @Query("SELECT * FROM userscore")
    fun getUserScoreRank(): PagingSource<Int, UserScore>

    @Query("SELECT * FROM UserScore WHERE userId = :userId")
    fun getCurrentUserScore(userId: Long): LiveData<UserScore>

    @Query("DELETE FROM userscore")
    fun cleanUserScoreRank()
}