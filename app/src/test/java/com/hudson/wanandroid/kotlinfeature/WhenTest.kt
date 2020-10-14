package com.hudson.wanandroid.kotlinfeature

import android.os.Build
import android.util.Log
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by Hudson on 2020/7/28.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class WhenTest{

    @Test
    fun testWhen(){
        println("result: ${fetch(getType())}")
    }

    private fun fetch(type: Int): Int{
        val str = when(type){
            1 -> "one"
            2 -> return 2 //这里直接整个方法完成，而不是when内完成
            else -> "other"
        }
        println("run here")
        return -2
    }

    private fun getType(): Int{
        return 2
    }

    @Test
    fun testSum(){
        // 结果会是 -0.09999999...8
        // flutter结果也是，有点奇怪，浮点运算导致
        // https://stackoverflow.com/questions/588004/is-floating-point-math-broken
        // https://stackoverflow.com/questions/15625556/adding-and-subtracting-doubles-are-giving-strange-results
        println("sum: ${ -1 + 0.9 }")
    }
}