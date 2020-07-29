package com.hudson.wanandroid.data.entity.wrapper

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
    var expireTime: Long
){
    constructor(clazz: Class<*>):this(clazz.name,"",0)

    var content:String? = null

}