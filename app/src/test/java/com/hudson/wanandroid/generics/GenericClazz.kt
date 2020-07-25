package com.hudson.wanandroid.generics

import java.lang.reflect.ParameterizedType

/**
 * Created by Hudson on 2020/7/25.
 */
open class GenericClazz<Data1, Data2>(val data1: Data1,
                                 val data2: Data2){

    init {
        var genericSuperclass = javaClass.genericSuperclass
        var type: ParameterizedType? = null
        while(genericSuperclass != null && type == null){
            if(genericSuperclass is ParameterizedType){
                type = genericSuperclass
            }else{
                genericSuperclass = (genericSuperclass as Class<*>).genericSuperclass
            }
        }
        if(type != null){
            val firstDataClass = type?.actualTypeArguments!![0]
            val secondDataClass = type?.actualTypeArguments!![1]
            println("generic class: $firstDataClass  $secondDataClass, current: $javaClass, type: $type")
        }else{
            println("cannot find generic type")
        }
    }
}