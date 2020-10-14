package com.hudson.wanandroid.data.common

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Hudson on 2020/9/21.
 */
class WanAndroidConfig(context: Context) {
    private val configuration: SharedPreferences

    init {
        configuration = context.getSharedPreferences(CONFIG_NAME, Context.MODE_PRIVATE)
    }

    fun getFlag(key: String, default: Boolean = false): Boolean{
        return configuration.getBoolean(key, default)
    }

    fun configFlag(key: String, value: Boolean){
        configuration.edit().putBoolean(key, value).apply()
    }

    fun getString(key: String, default: String? = null): String? {
        return configuration.getString(key, default)
    }

    fun configString(key: String, value: String){
        configuration.edit().putString(key, value).apply()
    }

    companion object{
        const val CONFIG_NAME = "wanandroidConfigs"
        const val COMMON_BROWSER_SEARCH_TOOL = "searchTool"
    }
}