package com.hudson.wanandroid.data.entity

import java.io.Serializable

/**
 * Created by Hudson on 2020/8/26.
 */
data class Subject(
    val children: List<Subject>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
): Serializable