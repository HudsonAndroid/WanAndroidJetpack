package com.hudson.wanandroid.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hudson.wanandroid.data.entity.StarArticle

/**
 * Created by Hudson on 2020/9/14.
 */
@Dao
interface StarArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStarArticles(articles: List<StarArticle>)

    @Query("SELECT * FROM StarArticle")
    fun getStarArticlePagingSource(): PagingSource<Int, StarArticle>

    @Query("DELETE FROM StarArticle WHERE id=:id")
    suspend fun unStarArticle(id: Int)

    @Query("DELETE FROM StarArticle")
    suspend fun cleanArticles()
}