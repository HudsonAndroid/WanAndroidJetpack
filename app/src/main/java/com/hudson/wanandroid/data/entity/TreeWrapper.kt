package com.hudson.wanandroid.data.entity

import com.hudson.wanandroid.data.entity.wrapper.BaseResult

/**
 * Created by Hudson on 2020/8/24.
 */
data class TreeWrapper(
    val data: List<TreeItem>
): BaseResult()

data class TreeItem(
    val children: List<TreeItem>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)