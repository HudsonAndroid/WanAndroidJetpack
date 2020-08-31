package com.hudson.wanandroid.ui.util

import android.widget.Toast
import com.hudson.wanandroid.WanAndroidApp

/**
 * Created by Hudson on 2020/8/31.
 */

var singleToast: Toast? = null

fun initialToast(){
    if(singleToast == null){
        singleToast = Toast.makeText(WanAndroidApp.GLOBAL_CONTEXT, "", Toast.LENGTH_SHORT)
    }
}

fun showToast(text: String) {
    initialToast()
    singleToast?.setText(text)
    singleToast?.show()
}

fun showToast(strId: Int) {
    initialToast()
    singleToast?.setText(strId)
    singleToast?.show()
}