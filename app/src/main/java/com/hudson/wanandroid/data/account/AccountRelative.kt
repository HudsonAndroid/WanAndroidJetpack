package com.hudson.wanandroid.data.account

import com.hudson.wanandroid.data.entity.LoginUser

/**
 * Created by Hudson on 2020/8/29.
 */
interface AccountRelative {
    /**
     * 账号首次初始化时触发，包括未登录情况下
     */
    fun onAccountInitialed(user: LoginUser?)

    /**
     * 当当前账号变动时触发
     */
    fun onAccountChanged(user: LoginUser)
}