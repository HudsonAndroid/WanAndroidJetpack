package com.hudson.wanandroid.data.repository

import android.content.Context
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.wrapper.BaseResult
import com.hudson.wanandroid.ui.util.showToast

/**
 * Created by Hudson on 2020/8/31.
 */
open class ArticleRepository(
    val wanAndroidApi: WanAndroidApi,
    val db: WanAndroidDb
){

    suspend fun starOrReverseArticle(context: Context, article: Article){
        var starFlag = true
        val result: BaseResult? = try {
            if(!article.collect){
                wanAndroidApi.starArticle(article.id)
            }else{
                starFlag = false
                wanAndroidApi.unStarArticle(article.id)
            }
        }catch (e: Exception){
            e.printStackTrace()
            null // invalid state, return null
        }
        if(result?.isSuccess() == true){
            article.collect = starFlag
            // we should update local star database
            // don't use insert, it will trigger item list-order mess
            db.articleDao().updateArticle(article)
        }else{
            showToast(R.string.tips_operate_failed)
            result?.tryHandleError(context)
        }
    }
}