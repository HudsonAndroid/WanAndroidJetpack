package com.hudson.wanandroid.data.account

import com.hudson.wanandroid.data.entity.LoginUser

/**
 * Created by Hudson on 2020/8/29.
 */
interface AccountRelative {

    fun onAccountChanged(user: LoginUser?)
}