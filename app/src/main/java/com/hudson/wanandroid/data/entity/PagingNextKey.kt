package com.hudson.wanandroid.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 用于记录整个项目中所有涉及到Paging的数据的
 * 下一页数据，用于RemoteMediator从网络指定
 * 页开始获取数据
 *
 * Created by Hudson on 2020/7/29.
 */
@Entity
data class PagingNextKey(
    @PrimaryKey
    val type: String,
    val nextKey: Int?
)