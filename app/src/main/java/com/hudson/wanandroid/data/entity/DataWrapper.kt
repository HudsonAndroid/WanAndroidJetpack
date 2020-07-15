package com.hudson.wanandroid.data.entity

import androidx.room.Entity

/**
 * Created by Hudson on 2020/7/14 0014.
 */
@Entity(
    primaryKeys = ["dataType","customInfo"]
)
class DataWrapper(
    val dataType: String,
    var customInfo: String = "",
    val expireTime: Long
){
    constructor(clazz: Class<Any>):this(clazz.name,"",0)

    lateinit var content:String

}