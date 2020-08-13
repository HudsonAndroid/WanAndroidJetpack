package com.hudson.wanandroid.viewmodel.global

import android.app.Activity
import android.view.View
import com.hudson.wanandroid.data.common.contextToActivity
import com.hudson.wanandroid.ui.activity.WebDetailActivity

/**
 * Created by Hudson on 2020/7/12.
 */
class GlobalJumpHandler{
    companion object{
        //绑定静态方法，注意，需要使用@JvmStatic，并且不是GlobalJumpHandler.Companion对象，而是直接调用
        @JvmStatic
        fun onJumpInvoked(view: View, url:String){
            WebDetailActivity.start(view.context,url)
        }

        @JvmStatic
        fun backPage(view: View){
            val activity = view.context.contextToActivity()
            activity.setResult(Activity.RESULT_CANCELED)
            activity.finish()
        }

    }
}