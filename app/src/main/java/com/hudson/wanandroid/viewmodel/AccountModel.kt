package com.hudson.wanandroid.viewmodel

import android.text.Editable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.account.WanAndroidAccount
import com.hudson.wanandroid.data.entity.LoginResult
import com.hudson.wanandroid.data.entity.wrapper.BaseResult
import com.hudson.wanandroid.ui.util.SimpleEditWatcher
import javax.inject.Inject

/**
 * Created by Hudson on 2020/8/29.
 */
class AccountModel @Inject constructor(private val api: WanAndroidApi): ViewModel(){
    val name = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirm = MutableLiveData<String>()
    val loadState = MutableLiveData<Boolean>()
    val loginEnable = MutableLiveData<Boolean>()
    val registerEnable = MutableLiveData<Boolean>()

    suspend fun login(): LoginResult?{
        loadState.value = true
        loginEnable.value = false
        val login = WanAndroidAccount.getInstance().login(name.value!!, password.value!!)
        loadState.value = false
        loginEnable.value = true
        return login
    }

    suspend fun register(): BaseResult?{
        loadState.value = true
        registerEnable.value = false
        val register = try {
            api.register(name.value!!, password.value!!, confirm.value!!)
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
        loadState.value = false
        registerEnable.value = true
        return register
    }

    val nameWatcher = object: SimpleEditWatcher(){
        override fun afterTextChanged(s: Editable?) {
            judgeBtnEnable()
        }
    }

    val passwordWatcher = object : SimpleEditWatcher(){
        override fun afterTextChanged(s: Editable?) {
            judgeBtnEnable()
        }
    }

    val confirmWatcher = object : SimpleEditWatcher(){
        override fun afterTextChanged(s: Editable?) {
            judgeBtnEnable()
        }
    }

    private fun judgeBtnEnable(){
        val loginFlag = !name.value.isNullOrEmpty() && !password.value.isNullOrEmpty()
        loginEnable.value = loginFlag
        registerEnable.value = loginFlag && !confirm.value.isNullOrEmpty()
    }
}