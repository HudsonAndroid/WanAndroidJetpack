package com.hudson.wanandroid.data.db

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.hudson.wanandroid.data.common.fromJson
import com.hudson.wanandroid.data.common.typedToJson
import com.hudson.wanandroid.data.entity.Tag

/**
 * Created by Hudson on 2020/7/29.
 */
class DbMemberConverters {
    @TypeConverter
    fun class2String(clazz: Class<*>) : String = clazz.name

    @TypeConverter
    fun string2Class(input: String): Class<*> = Class.forName(input)

    @TypeConverter
    fun articleTag2Json(input: List<Tag>) : String =
        GsonBuilder().create().typedToJson(input)

    @TypeConverter
    fun json2ArticleTag(json: String): List<Tag>? =
        GsonBuilder().create().fromJson(json)
}