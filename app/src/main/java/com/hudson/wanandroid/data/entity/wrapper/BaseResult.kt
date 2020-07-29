package com.hudson.wanandroid.data.entity.wrapper

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

}