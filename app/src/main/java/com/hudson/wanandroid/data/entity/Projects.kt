package com.hudson.wanandroid.data.entity

import com.hudson.wanandroid.data.entity.wrapper.BaseResult

/**
 * Created by Hudson on 2020/8/14.
 */
data class Projects(
    val data: List<ProjectInfo>
): BaseResult()

// 注意，该类忽略了服务器端的children字段，因为不清楚children具体的数据类型
data class ProjectInfo(
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Long,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)