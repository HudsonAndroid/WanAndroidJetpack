package com.hudson.wanandroid.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import okhttp3.Cookie

/**
 * 自定义登录账号对象，对应数据库存储实例
 * Created by Hudson on 2020/8/28.
 */
@Entity
data class LoginUser(
    @PrimaryKey
    val id: Int,
    val loginInfo: LoginInfo,
    val cookies: List<Cookie>,
    var current: Boolean = false
)