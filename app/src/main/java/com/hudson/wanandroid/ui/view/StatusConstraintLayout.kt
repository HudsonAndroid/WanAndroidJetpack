package com.hudson.wanandroid.ui.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout


/**
 * 可以将fitsSystemWindows属性传递给子view的ConstraintLayout
 *
 * https://stackoverflow.com/questions/39307674/android-fitssystemwindows-not-working-when-replacing-fragments
 * Created by Hudson on 2020/7/31.
 */
class StatusConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr){

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets? {
        val childCount = childCount
        // let children know about WindowInsets
        for (index in 0 until childCount) getChildAt(index).dispatchApplyWindowInsets(insets)
        return insets
    }
}