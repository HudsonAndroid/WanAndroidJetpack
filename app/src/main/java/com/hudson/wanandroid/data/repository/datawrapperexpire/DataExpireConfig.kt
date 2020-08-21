package com.hudson.wanandroid.data.repository.datawrapperexpire

import com.hudson.wanandroid.data.entity.Banner
import com.hudson.wanandroid.data.entity.ArticleResultWrapper
import com.hudson.wanandroid.data.entity.TopArticle

/**
 * Created by Hudson on 2020/7/16 0016.
 */
class DataExpireConfig {

    companion object{
        private val expireConfig = mutableMapOf<Class<*>, Int>()
        private const val DAY_MILL_SECONDS: Long = 24*60*60*1000
        // 0表示永久有效, NetworkBoundResource如果发现ROOM中有，便不会访问网络
        private const val DEFAULT_FOREVER_AGE = 0

        init { //相当于静态代码块
            expireConfig[Banner::class.java] = 1
            expireConfig[TopArticle::class.java] = 1
            expireConfig[ArticleResultWrapper::class.java] = 1
        }

        fun getAge(clazz: Class<*>): Long{
            var source =
                DEFAULT_FOREVER_AGE
            if(expireConfig.containsKey(clazz)){
                source = expireConfig[clazz]?: DEFAULT_FOREVER_AGE
            }
            return source * DAY_MILL_SECONDS
        }
    }
}