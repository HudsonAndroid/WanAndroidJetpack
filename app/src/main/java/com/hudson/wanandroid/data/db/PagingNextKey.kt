package com.hudson.wanandroid.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用于记录整个项目中所有涉及到Paging的数据的
 * 下一页数据，用于RemoteMediator从网络指定
 * 页开始获取数据
 *
 * 依赖 [DbMemberConverters.class2String]
 * 和 [DbMemberConverters.string2Class]
 * Created by Hudson on 2020/7/29.
 */
@Entity
data class PagingNextKey(
    @PrimaryKey
    val type: Class<*>,
    val nextKey: Int?
)