package com.hudson.wanandroid.data.account

import androidx.lifecycle.MutableLiveData
import com.hudson.wanandroid.WanAndroidApp
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.LoginInfo
import com.hudson.wanandroid.data.entity.LoginResult
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.wrapper.BaseResult
import okhttp3.Cookie
import kotlin.Exception

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
            currentUser.postValue(user)
            if(user != null){
                cookieCache = user.cookies.toMutableList()
            }
        }
    }

    suspend fun login(userName: String, password:String): LoginResult? {
        val login = try{
            api.login(userName, password)
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
        if (login?.isSuccess() == true) {
            val currentLogin = login.data
            if(isMatch(currentLogin, cookieCache)){
                switchAccount(LoginUser(currentLogin.id, currentLogin, cookieCache!!))
            }else{
                // clean
                cookieCache = mutableListOf()
            }
        }
        return login
    }

    suspend fun logout(): BaseResult? {
        val result = try{
            api.logout()
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
        if(result?.isSuccess() == true){
            db.loginUserDao().removeCurrentUser()
            cookieCache = mutableListOf()
            currentUser.value = null
        }
        return result
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

    fun accountList() = db.loginUserDao().allUsers()

    suspend fun switchAccount(accountId: Long){
        switchAccount(db.loginUserDao().getUser(accountId))
    }

    private suspend fun switchAccount(targetUser: LoginUser){
        // todo 可以考虑是否要判断账号是否还是旧账号
        val modifiedUsers = mutableListOf<LoginUser>()
        if(currentUser.value != null){
            // clean old user flag
            currentUser.value?.current = false
            modifiedUsers.add(currentUser.value!!)
        }
        // change current user flag
        targetUser.current = true
        modifiedUsers.add(targetUser)
        // update change into Room
        db.loginUserDao().insertUsers(modifiedUsers)
        // switch now
        cookieCache = targetUser.cookies.toMutableList()
        currentUser.value = targetUser
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