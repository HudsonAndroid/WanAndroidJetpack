package com.hudson.wanandroid.kotlinfeature

import android.os.Build
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Created by Hudson on 2020/12/21.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class LambdaTest {

    @Test
    fun testLambda(){
//        mulByThree(2){
//            // it表示的是lambda的参数
//            print("运行结果是$it")
//            return  // 由于内联函数的特性，导致testLambda直接结束了
//        }
        mulByFour(2){
            println("运行crossinline$it")
            // crossinline要求返回值必须是int类型
            66  // 这个作为最终的返回结果，不需要写return
        }
        println("完成内联函数调用")
        notInlineFunction(3){
            print("调用非内联函数$it")
//            return // 报错，非内联函数中的lambda是不允许使用return的，但可以下面这种方式，表示退出这个lambda表达式
            return@notInlineFunction
        }
    }

    // 传入的lambda中不能使用return 表达式
    fun notInlineFunction(
        num: Int,
        function: (result: Int) -> Unit) {
        val result = num * 4
        function(result)
    }

    // noinline用于修饰inline方法的lambda参数，以让inline特性不作用到lambda参数上
    inline fun mulByTwo(
        num: Int,
        /*noinline*/ function: (result: Int) -> Unit):Int {
        val result = num * 2
//        function.invoke(result) 可以调用invoke来调用lambda表达式，也可以直接像调用方法一样调用
        function(result)
        return result
    }

    inline fun mulByThree(
        num: Int,
        function: (result: Int) -> Int) {// lambda表达式要求返回值是Int类型，但由于没有crossinline的修饰，内部局部返回，即直接return，导致没有返回结果
        val result = num * 3
        val lambdaResult = function(result)
        println("lambda表达式运行结果$lambdaResult")
    }

    inline fun mulByFour(
        num: Int,
        crossinline function: (result: Int) -> Int){
        val result = num * 4
        val lambdaResult = function(result)
        println("lambda表达式运行结果$lambdaResult")
    }


    @Test
    fun testLambda2(){
        var test2 = test2()
        println(" "+test2)
        println(test3())
    }

    private fun test1() {// 如果外部的没有返回值，下面代码是可以正常的
        mulByThree(4){
            println("结果$it")
            return
        }
    }

    private fun test2(): Int {
        mulByThree(5){
            println("乘积是$it")
            // 如果这里没有返回值，将会报错，原因呢并不是因为lambda表达式要求返回Int，而是外部方法要求返回int，
            // 因为内联函数内部的return实际上会被复制到test2方法中，也就是说最后test2()的结果是5
            return 5
        }
        return 1 // 这里将永远执行不到
    }

    // 那lambda中的return 和lambda实际要返回的值怎么区分呢
    private fun test3(): Int {
        mulByThree(6){
            println("乘积$it")
            696 // 实际上lambda表达式的返回是不需要return的，而是写在最后一行的
        }
        // 真正的返回在这里
        return 3
    }

    private fun test4(): Int {
        mulByFour(7){
//            return 4 // 不允许使用return，最多只能返回lambda表达式需要的返回值
            4
        }
        return 4
    }
}