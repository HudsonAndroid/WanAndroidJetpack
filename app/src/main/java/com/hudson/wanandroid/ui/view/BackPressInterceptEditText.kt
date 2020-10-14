package com.hudson.wanandroid.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText
import timber.log.Timber

/**
 * 注意： EditText和其他自定义view不同，不能按照之前那样重新构造方法
 * this(context,attrs,0);不可使用这种方式，因为第三个参数系统有默认值传入
 * Created by Hudson on 2020/9/22.
 */
class BackPressInterceptEditText constructor(
    context: Context, attrs: AttributeSet?
): AppCompatEditText(context,attrs){

    constructor(context: Context): this(context, null)

    var dismissKeyboardListener: OnDismissKeyboardListener? = null

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent?): Boolean {
        // 监听有打开输入法情况下第一次返回时原有关闭输入法操作
        if (event != null && event.keyCode == KeyEvent.KEYCODE_BACK
            && event.action == KeyEvent.ACTION_UP) {
            return dismissKeyboardListener?.onDismissKeyboard() ?: false
        }
        return super.onKeyPreIme(keyCode, event)
    }
}

interface OnDismissKeyboardListener{
    fun onDismissKeyboard(): Boolean
}