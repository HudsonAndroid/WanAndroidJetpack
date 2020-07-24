package com.hudson.wanandroid.data.entity

/**
 * Created by Hudson on 2020/7/21 0021.
 */
data class Tag(
    val name: String,
    val url: String
){
    override fun toString(): String {
        return "Tag(name='$name', url='$url')"
    }
}