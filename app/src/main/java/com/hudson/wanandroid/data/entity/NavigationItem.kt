package com.hudson.wanandroid.data.entity

import com.hudson.wanandroid.data.entity.wrapper.BaseResult

/**
 * Created by Hudson on 2020/8/25.
 */
data class NavigationItem(
    val cid: Int,
    val name: String,
    val articles: List<Article>
)

data class NavigationWrapper(
    val data: List<NavigationItem>
): BaseResult()