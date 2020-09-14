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
    val username: String,
    // 2020-9-14
    // 发现ROOM中的数据如果已经存在，通过Paging访问时
    // 会出现把已有数据（当前账号的积分数据）当做Paging中一页的情况，之后如果
    // 刷新Paging，会把之前的Paging该页数据覆盖，而且
    // 包括了之前的已有数据，即当前用户的积分数据丢失了！！
    // 因此给从Paging上获取的数据添加一个Tag，在查询PagingSource时条件查询
    var pagingTag: String? = ""
    // 上面数据要允许为null，因为通通过Gson转换的数据该字段将是null的
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