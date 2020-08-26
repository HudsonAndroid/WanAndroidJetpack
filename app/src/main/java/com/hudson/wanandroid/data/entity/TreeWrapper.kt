package com.hudson.wanandroid.data.entity

import com.hudson.wanandroid.data.entity.wrapper.BaseResult

/**
 * Created by Hudson on 2020/8/24.
 */
data class TreeWrapper(
    val data: List<Subject>
): BaseResult()