package com.hudson.wanandroid.data.entity

import com.hudson.wanandroid.data.entity.wrapper.BaseResult

/**
 * Created by Hudson on 2020/7/21.
 */
data class ArticleResultWrapper(
    val data: ArticleWrapper
): BaseResult(){
    override fun toString(): String {
        return "ArticleResult(data=$data)"
    }
}

data class ArticleWrapper(
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: List<Article>
){
    override fun toString(): String {
        return "ArticleWrapper(curPage=$curPage, offset=$offset, over=$over, pageCount=$pageCount, size=$size, total=$total, datas=$datas)"
    }
}