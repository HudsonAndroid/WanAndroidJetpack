package com.hudson.wanandroid.kotlinfeature

import android.os.Build
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * 在kotlin中可以给方法部分参数为默认值，调用时就可以不传该参数而使用默认值
 * Created by Hudson on 2020/12/29.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class MethodTest {

    @Test
    fun test(){
        var method1 = method1("66")
        print(method1)
    }

    fun method1(a: String, b:Int? = null): String{
        if(b == null) return a
        return a + b
    }
}