package com.hudson.wanandroid.data.account

import androidx.lifecycle.MutableLiveData
import com.hudson.wanandroid.WanAndroidApp
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.LoginInfo
import com.hudson.wanandroid.data.entity.LoginUser
import okhttp3.Cookie

/**
 * 账号模块
 * Created by Hudson on 2020/8/28.
 */
// TODO: 2020/8/28 0028 在Application中创建了一个全局的context，代码侵入性较高，可考虑优化
class WanAndroidAccount private constructor(
    private val api: WanAndroidApi,
    private val db: WanAndroidDb){

    val currentUser = MutableLiveData<LoginUser>()

    init {
        AppExecutor.getInstance().ioExecutor.execute {
            val user = db.loginUserDao().getCurrentUser()
            user?.initialState = true
            currentUser.postValue(user)
            if(user != null){
                cookieCache = user.cookies.toMutableList()
            }
        }
    }

    suspend fun login(userName: String, password:String) {
        val login = api.login(userName, password)
        if (login.isSuccess()) {
            val currentLogin = login.data
            if(isMatch(currentLogin, cookieCache)){
                val loginUserDao = db.loginUserDao()
                if(currentUser.value != null){
                    currentUser.value?.current = false
                    // clean old user flag
                    loginUserDao.insertUser(currentUser.value!!)
                }
                // save account info into Room
                val user = LoginUser(currentLogin.id, currentLogin, cookieCache!!, true)
                currentUser.value = user
                loginUserDao.insertUser(user)
            }else{
                // clean
                cookieCache = mutableListOf()
            }
        }
    }

    // 检查当前的登录账号和获取到的cookie是否对应
    private fun isMatch(loginInfo: LoginInfo, cookies: MutableList<Cookie>?): Boolean{
        if(cookies != null){
            for (cookie in cookies) {
                if(cookie.value() == loginInfo.username){
                    return true
                }
            }
        }
        return false
    }

    suspend fun accountList() = db.loginUserDao().allUsers()

    suspend fun switchAccount(accountId: Int){
        // change current account
        val loginUserDao = db.loginUserDao()
        val user = loginUserDao.getUser(accountId)
        user.current = true
        loginUserDao.insertUser(user)
        currentUser.value = user
        cookieCache = user.cookies.toMutableList()
    }

    companion object{
        @Volatile
        private var instance: WanAndroidAccount? = null
        var cookieCache: MutableList<Cookie>? = null

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