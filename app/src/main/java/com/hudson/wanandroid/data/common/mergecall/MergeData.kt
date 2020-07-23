package com.hudson.wanandroid.data.common.mergecall

/**
 * Created by Hudson on 2020/7/21.
 */
open class MergeData<Data1, Data2> (
    var data1: Data1?,
    var data2: Data2?
){
    constructor(): this(null,null)

    fun isEmpty(): Boolean{
        return data1 == null && data2 == null
    }

    override fun toString(): String {
        return "MergeData(data1=$data1, data2=$data2)"
    }

}