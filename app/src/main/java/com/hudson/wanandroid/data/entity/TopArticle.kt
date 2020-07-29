package com.hudson.wanandroid.data.entity

import com.hudson.wanandroid.data.entity.wrapper.BaseResult

/**
 * Created by Hudson on 2020/7/21.
 */
data class TopArticle(
    val data: List<Article>
): BaseResult()