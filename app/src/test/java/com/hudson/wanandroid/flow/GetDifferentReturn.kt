package com.hudson.wanandroid.flow

import android.os.Build
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * 在suspend函数中异步仅返回单个结果，
 * flow可以用于异步计算返回多个结果。
 * 这里列举出返回多个结果的方式。
 *
 * Flow和Sequence在没有collect之前，内部
 * 代码是不会执行的，而且每次collect都会执行
 * 一次
 * Created by Hudson on 2020/8/5.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class GetDifferentReturn {

    @Test
    fun testCollections(){
        val list = listOf<Int>(1,2,3)
        list.forEach {
            number -> println(number)
        }
    }

    @Test
    fun testSequence(){
        val sequence = sequence<Int> { // Sequence构建器
            for (i in 1..3){
                Thread.sleep(50) //在主线程中运行
                yield(i)
            }
        }

        sequence.forEach {
            number -> println(number)
        }
    }

    @Test
    fun testFlow(){
        runBlocking {
            launch {
                for (j in 4..6){
                    println(j)
                    delay(50)
                }
            }
            launch {
                flow<Int> { // flow 构建器构建一个flow，在内部可以使用suspend函数(delay)
                    for(i in 1..3){
                        delay(50) //异步
                        emit(i) //相当于RxJava中的onNext
                    }
                }.collect{//相当于subscribe
                    println(it)
                }

//            flowOf(1,2,3,4)
//                .onEach {
//                    delay(100)
//                }
//                .collect {
//                    println(it)
//                }
            }
        }
    }

    @Test
    fun testFlow2() {
        val flow = flow<Int> {
            println("flow run start")
            for (i in 1..3){
                delay(50)
                emit(i)
            }
        }
        runBlocking {
            println("call collect first time")
            flow.collect{ number -> println(number) }
            println("call collect second time")
            flow.collect{ number -> println(number) }
        }
    }

    @Test
    fun testFlowCancel(){
        val flow = flow<Int> {
            for (i in 1..3){
                delay(50)
                println("emit $i")
                emit(i)
            }
        }

        runBlocking {
            withTimeoutOrNull(100) {
                flow.collect{ value -> println(value) }
            }
            println("completed")
        }
    }

    @Test
    fun testFlowCreate(){
        // 创建方式除了上面的方式之外，还有flowOf，或者集合类、Sequence通过.asFlow转为flow
        val flow1 = flowOf(1, 2, 3)
        val flow2 = (1..3).asFlow()
        val flow3 = sequence<Int> {
            for (i in 1..3) {
                Thread.sleep(50) //在主线程中运行
                yield(i)
            }
        }.asFlow()
    }
}