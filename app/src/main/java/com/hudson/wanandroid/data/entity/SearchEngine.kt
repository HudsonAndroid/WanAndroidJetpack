package com.hudson.wanandroid.data.entity

import com.hudson.wanandroid.R

/**
 * Created by Hudson on 2020/9/25.
 */
data class SearchEngine(
    val name: Int,
    val link: String,
    var icon: String?,
    var current: Boolean = false
)

val baiduEngine = SearchEngine(R.string.title_browser_search_baidu,
    "https://www.baidu.com/s?wd=", null, true)

val googleEngine = SearchEngine(R.string.title_browser_search_google,
    "https://www.google.com/search?q=", null, false)