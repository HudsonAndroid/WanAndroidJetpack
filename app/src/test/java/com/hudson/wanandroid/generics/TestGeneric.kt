package com.hudson.wanandroid.generics

import android.os.Build
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * getGenericSuperclass  返回直接继承的父类（包含泛型参数）
 * 也就是说必须是父子类的关系才能正常获取类型
 *
 * 因此，外界在使用MergeCall的时候，必须按照两种方案来：
 *  1.按照下面方式，必须创建一个MergeCall的匿名（或者实名也未必不可）子类实例
 *  2.在构造MergeCall的时候，指明实际的泛型参数类型
 *
 * 综合使用者考虑，方式2更为方便，同时为了简化构建，利用Builder模式协助，因此
 * 修改MergeCall实现。
 * Created by Hudson on 2020/7/25.
 */
@Config(sdk = [Build.VERSION_CODES.O_MR1])
@RunWith(RobolectricTestRunner::class)
class TestGeneric {

    @Test
    fun testTwoGenerics(){
        var data1 = Data1("data1")
        var data2 = Data2("data2")
        // cannot get generic class type
//        val genericClazz = GenericClazz(data1, data2)
        // cannot get generic class type
//        val genericClazz2 = GenericClazz2<Data1, Data2>()

//        // success
//        val genericClazz3 = GenericClazz3()

        // success
//        val genericClazz4 = object: GenericClazz2<Data1,Data2>(){
//
//        }

        // success
        val genericClazz5 = object: GenericClazz<Data1, Data2>(data1, data2){

        }
    }
}