package com.hudson.wanandroid.data.common.mergecall

/**
 * Created by Hudson on 2020/7/21.
 */
open class MergeData<Data1, Data2> (
    var data1: Data1?,
    var data2: Data2?
){
    var networkData1Empty = false
    var networkData2Empty = false

    constructor(): this(null,null)

    fun isEmpty(): Boolean{
        return data1 == null && data2 == null
    }

    fun isDataFresh(): Boolean{
        return !networkData1Empty || !networkData2Empty
    }

    override fun toString(): String {
        return "MergeData(data1=$data1, data2=$data2)"
    }

}