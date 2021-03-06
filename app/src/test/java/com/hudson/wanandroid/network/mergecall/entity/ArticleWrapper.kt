package com.hudson.wanandroid.network.mergecall.entity

import com.hudson.wanandroid.data.common.mergecall.MergeData
import com.hudson.wanandroid.data.entity.ArticleResultWrapper
import com.hudson.wanandroid.data.entity.TopArticle

/**
 * Created by Hudson on 2020/7/22.
 */
class ArticleWrapper(
    topArticle: TopArticle?,
    articleResultWrapper: ArticleResultWrapper?
): MergeData<TopArticle, ArticleResultWrapper>(topArticle, articleResultWrapper)