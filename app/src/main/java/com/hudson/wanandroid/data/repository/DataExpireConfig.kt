package com.hudson.wanandroid.data.repository

import com.hudson.wanandroid.data.entity.Banner

/**
 * Created by hpz on 2020/7/16 0016.
 */
class DataExpireConfig {

    companion object{
        private val expireConfig = mutableMapOf<Class<*>, Int>()
        private const val DAY_MILL_SECONDS: Long = 24*60*60*1000
        private const val DEFAULT_AGE = 0

        init { //相当于静态代码块
            expireConfig[Banner::class.java] = 1
        }

        fun getAge(clazz: Class<*>): Long{
            var source = DEFAULT_AGE
            if(expireConfig.containsKey(clazz)){
                source = expireConfig[clazz]?: DEFAULT_AGE
            }
            return source * DAY_MILL_SECONDS
        }
    }
}