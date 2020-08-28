package com.hudson.wanandroid.data.account

import com.hudson.wanandroid.WanAndroidApp
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.LoginInfo
import com.hudson.wanandroid.data.entity.LoginUser
import okhttp3.Cookie
import timber.log.Timber

/**
 * 账号模块
 * Created by Hudson on 2020/8/28.
 */
// TODO: 2020/8/28 0028 在Application中创建了一个全局的context，代码侵入性较高，可考虑优化
class WanAndroidAccount private constructor(
    private val api: WanAndroidApi,
    private val db: WanAndroidDb){

    var currentLogin: LoginInfo? = null
    var cookies = mutableListOf<Cookie>()
    private var loginUser: LoginUser? = null

    init {
        AppExecutor.getInstance().ioExecutor.execute {
            val user = db.loginUserDao().getCurrentUser()
            if(user != null) {
                Timber.e("当前账号是$user")
                currentLogin = user.loginInfo
                cookies = user.cookies.toMutableList()
            }
        }
    }

    suspend fun login(userName: String, password:String) {
        val login = api.login(userName, password)
        if (login.isSuccess()) {
            currentLogin = login.data
            Timber.e("登录完成")
            if(isMatch(currentLogin!!, cookies)){
                val loginUserDao = db.loginUserDao()
                if(loginUser != null){
                    loginUser?.current = false
                    // clean old user flag
                    loginUserDao.insertUser(loginUser!!)
                }
                // save account info into Room
                loginUserDao.insertUser(LoginUser(currentLogin!!.id, currentLogin!!, cookies, true))
            }
        }
    }

    // 检查当前的登录账号和获取到的cookie是否对应
    private fun isMatch(loginInfo: LoginInfo, cookies: MutableList<Cookie>): Boolean{
        for (cookie in cookies) {
            if(cookie.value() == loginInfo.username){
                return true
            }
        }
        return false
    }

    suspend fun accountList() = db.loginUserDao().allUsers()

    suspend fun switchAccount(accountId: Int){
        // change current account
        val user = db.loginUserDao().getUser(accountId)
        currentLogin = user.loginInfo
        cookies = user.cookies.toMutableList()
    }

    companion object{
        @Volatile
        private var instance: WanAndroidAccount? = null

        fun getInstance(): WanAndroidAccount {
            return instance?: synchronized(this){
                instance?: WanAndroidAccount(WanAndroidApi.api(),
                    WanAndroidDb.getInstance(WanAndroidApp.GLOBAL_CONTEXT!!)).also {
                    instance = it
                }
            }
        }
    }

}