package com.hudson.wanandroid.network.common

import java.io.File

/**
 * Created by Hudson on 2020/7/24.
 */
// 注意：需要替换成对应的盘符和路径
private const val LOCAL_BASE_PATH = "F:/projects/WanAndroidJetpack/app/src/test/java/resources/"

fun readFile(path: String) : String{
    return File(LOCAL_BASE_PATH + path).readText(Charsets.UTF_8)
}