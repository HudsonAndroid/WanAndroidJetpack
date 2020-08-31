package com.hudson.wanandroid.data.db

import androidx.paging.PagingSource
import androidx.room.*
import com.hudson.wanandroid.data.entity.Article

/**
 * Created by Hudson on 2020/7/29.
 */
@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    // 注意，update和OnConflictStrategy.REPLACE不一样，前者会出现数据重新掉到PagingSource一页数据的最后
    @Update
    suspend fun updateArticle(article: Article)

    //返回值PagingSource类型要求room版本2.3.0-alpha01
    @Query("SELECT * FROM article")
    fun getArticlePagingSource(): PagingSource<Int, Article>

    @Query("SELECT * FROM article WHERE chapterId = :projectId")
    fun getProjectPagingSource(projectId: Int): PagingSource<Int, Article>

    @Query("SELECT * FROM article WHERE chapterId = :cid AND realSuperChapterId = :superId")
    fun getSubjectPagingSource(cid: Int, superId: Int): PagingSource<Int, Article>

    @Query("DELETE FROM article")
    suspend fun cleanArticles()

    @Query("DELETE FROM article WHERE chapterId = :projectId")
    suspend fun cleanTargetArticles(projectId: Int)

    @Query("DELETE FROM article WHERE chapterId = :cid AND realSuperChapterId = :superId")
    suspend fun cleanTargetArticles(cid: Int, superId: Int)
}