package com.hudson.wanandroid.data.entity.wrapper

import android.content.Context
import android.widget.Toast
import com.hudson.wanandroid.ui.activity.LoginActivity
import com.hudson.wanandroid.ui.util.showToast

/**
 * Created by Hudson on 2020/7/11.
 */
open class BaseResult {
    var errorCode : Int = -1
    var errorMsg: String = ""


    override fun toString(): String {
        return "BaseResult(errorCode=$errorCode, errorMsg='$errorMsg')"
    }

    open fun isSuccess():Boolean{
        return errorCode == 0
    }

    open fun tryHandleError(context: Context){
        if(errorCode == -1001){
            showToast(errorMsg)
            LoginActivity.start(context)
        }
    }

}