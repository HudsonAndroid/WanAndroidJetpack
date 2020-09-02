package com.hudson.wanandroid.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hudson.wanandroid.data.entity.wrapper.BaseResult

/**
 * Created by Hudson on 2020/9/1.
 */
@Entity
data class UserScore(
    val coinCount: Long,
    val level: Int,
    val rank: Long,
    @PrimaryKey
    val userId: Long,
    val username: String
)

data class CurrentUserScore(
    val data: UserScore
): BaseResult()

data class UserScoreRankWrapper(
    val data: UserScoreRank
): BaseResult()

data class UserScoreRank(
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Long,
    val curPage: Int,
    val datas: List<UserScore>
)