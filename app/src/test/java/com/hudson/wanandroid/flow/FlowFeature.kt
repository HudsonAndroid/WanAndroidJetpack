package com.hudson.wanandroid.flow

import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.Exception

/**
 * Created by Hudson on 2020/8/5.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class FlowFeature {

    private fun flowCreator() : Flow<ResultWrapper<Int>> {
        return flow {
            var tmp: Int? = null
            try{
                for (i in 1..3){
                    delay(500)
                    emit(ResultWrapper.Success(i))
                    if(i == 3){
                        tmp = 0
                    }
                }
                // create an error
                tmp?.run {
                    3 / this
                }
            }catch (e: Exception){
                emit(ResultWrapper.Error(e))
            }
            // outer exception
            tmp?.run {
                3 / this
            }
        }
    }

    @Test
    fun testFlow(){
        runBlocking {
            flowCreator()
                .onStart {
                    println("test start...") //运行前的操作，一般情况下显示一个正在加载的进度条
                }
                .catch { e ->
                    println("catch exception $e") //flow中出现未捕获异常时的处理
                }
                .onCompletion {
                    println("test completed") //完成时操作
                }
                .collectLatest { resultWrapper ->
                    // flow内部emit出来的结果
                    if(resultWrapper is ResultWrapper.Success){
                        println("success")
                    }else if( resultWrapper is ResultWrapper.Error){
                        println("error")
                    }
                }
        }
    }
}