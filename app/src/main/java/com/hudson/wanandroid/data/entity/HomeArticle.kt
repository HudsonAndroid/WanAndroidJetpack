package com.hudson.wanandroid.data.entity

/**
 * Created by Hudson on 2020/7/21.
 */
data class HomeArticle(
    val data: HomeArticleWrapper
): BaseResult(){
    override fun toString(): String {
        return "HomeArticle(data=$data)"
    }
}

data class HomeArticleWrapper(
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: List<Article>
){
    override fun toString(): String {
        return "HomeArticleWrapper(curPage=$curPage, offset=$offset, over=$over, pageCount=$pageCount, size=$size, total=$total, datas=$datas)"
    }
}