package com.hudson.wanandroid.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hudson.wanandroid.data.entity.Article

/**
 * Created by Hudson on 2020/7/29.
 */
@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)

    //返回值PagingSource类型要求room版本2.3.0-alpha01
    @Query("SELECT * FROM article")
    fun getArticlePagingSource(): PagingSource<Int, Article>

    @Query("DELETE FROM article")
    suspend fun cleanArticles()
}