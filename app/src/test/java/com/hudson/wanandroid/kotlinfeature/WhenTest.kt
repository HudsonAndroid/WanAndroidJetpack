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
}