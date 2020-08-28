package com.hudson.wanandroid.data.db

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.hudson.wanandroid.data.common.fromJson
import com.hudson.wanandroid.data.common.typedToJson
import com.hudson.wanandroid.data.entity.LoginInfo
import com.hudson.wanandroid.data.entity.Tag
import okhttp3.Cookie

/**
 * Created by Hudson on 2020/7/29.
 */
class DbMemberConverters {
//    @TypeConverter
//    fun class2String(clazz: Class<*>) : String = clazz.name
//
//    @TypeConverter
//    fun string2Class(input: String): Class<*> = Class.forName(input)

    @TypeConverter
    fun articleTag2Json(input: List<Tag>): String =
        GsonBuilder().create().typedToJson(input)

    @TypeConverter
    fun json2ArticleTag(json: String): List<Tag>? =
        GsonBuilder().create().fromJson(json)

    @TypeConverter
    fun loginInfo2Json(loginInfo: LoginInfo): String =
        GsonBuilder().create().typedToJson(loginInfo)

    @TypeConverter
    fun json2LoginInfo(json: String): LoginInfo =
        GsonBuilder().create().fromJson(json)

    @TypeConverter
    fun cookieList2Json(cookies: List<Cookie>): String =
        GsonBuilder().create().typedToJson(cookies)

    @TypeConverter
    fun json2CookieList(json: String): List<Cookie> =
        GsonBuilder().create().fromJson(json)
}