package com.hudson.wanandroid.data.entity

import com.hudson.wanandroid.data.entity.wrapper.BaseResult

/**
 * Created by Hudson on 2020/8/27.
 */
data class LoginResult(
    val data: LoginInfo
): BaseResult()

// TODO: 2020/8/27 chapterTops不知道具体类型
data class LoginInfo(
    val admin: Boolean,
    val coinCount: Int,
//    val chapterTops: List<?>,
    val collectIds: List<Int>,
    val email: String,
    var icon: String,
    val id: Long,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)