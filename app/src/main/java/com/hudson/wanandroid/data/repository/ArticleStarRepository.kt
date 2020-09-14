package com.hudson.wanandroid.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.StarArticle
import com.hudson.wanandroid.data.entity.wrapper.BaseResult
import com.hudson.wanandroid.data.repository.paging.StarRemoteMediator
import com.hudson.wanandroid.ui.util.showToast
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Hudson on 2020/9/14.
 */
@Singleton
class ArticleStarRepository @Inject constructor(
    private val wanAndroidApi: WanAndroidApi,
    private val db: WanAndroidDb
){

    fun loadStarArticles() = Pager(config = PagingConfig(pageSize = LOAD_PAGING_SIZE),
        remoteMediator = StarRemoteMediator(wanAndroidApi, db)){
        db.starArticleDao().getStarArticlePagingSource()
    }.flow

    suspend fun unStarArticle(context: Context, article: StarArticle){
        val result: BaseResult? = try {
            wanAndroidApi.unStarArticleUseStarId(article.id, article.originId ?: -1)
        }catch (e: Exception){
            e.printStackTrace()
            null // invalid state, return null
        }
        if(result?.isSuccess() == true){
            // firstly, we should remove StarArticle from StarArticle table
            db.starArticleDao().unStarArticle(article.id)
            // then, we should remove truly Article from Article table
            article.originId?.run {
                // 收藏获取的article的originId相当于普通文章的id
                val targetArticle = db.articleDao().getTargetArticle(this)
                targetArticle?.run {
                    targetArticle.collect = false
                    db.articleDao().insertArticle(targetArticle)
                }
            }
        }else{
            showToast(R.string.tips_operate_failed)
            result?.tryHandleError(context)
        }
    }

    companion object{
        private const val LOAD_PAGING_SIZE = 20
    }
}