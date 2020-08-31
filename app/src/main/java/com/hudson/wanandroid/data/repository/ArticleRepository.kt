package com.hudson.wanandroid.data.repository

import android.content.Context
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Article

/**
 * Created by Hudson on 2020/8/31.
 */
open class ArticleRepository(
    val wanAndroidApi: WanAndroidApi,
    val db: WanAndroidDb
){

    suspend fun starOrReverseArticle(context: Context, article: Article){
        var starFlag = true
        val result = if(!article.collect){
            wanAndroidApi.starArticle(article.id)
        }else{
            starFlag = false
            wanAndroidApi.unStarArticle(article.id)
        }
        if(result.isSuccess()){
            article.collect = starFlag
            // we should update local star database
            // don't user insert, it will trigger item list-order mess
            db.articleDao().updateArticle(article)
        }else{
            result.tryHandleError(context)
        }
    }
}