package com.hudson.wanandroid.kotlinfeature

import android.os.Build
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * kotlin内联扩展函数let with run apply also
 * Created by Hudson on 2020/12/21.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class KotlinRunWithLet {

    @Test
    fun test(){
        // 验证let （明确指明返回）
        val target = TestClass()
        val sum = target?.let {
            100 + it.age
        }
        // 验证with （明确指明返回）
        val totalAge = with(target){
            name = "Hudson666"
            200 + age
        }
        // 验证run （明确指明返回）
        val name = target?.run{
            age = 18
            name
        }
        // 验证apply （返回自身）
        target?.apply {
            age = 18
        }
        // 验证also （返回自身，与let类似）
        target?.also {
            it.age = 18
        }
    }
}


class TestClass {
    var name: String="Hudson"
    var age:Int=18

}