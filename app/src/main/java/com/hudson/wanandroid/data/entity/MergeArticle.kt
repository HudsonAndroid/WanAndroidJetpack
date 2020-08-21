package com.hudson.wanandroid.data.entity

import com.hudson.wanandroid.data.common.mergecall.MergeData

/**
 * Created by Hudson on 2020/7/22.
 */
class MergeArticle(
    topArticle: TopArticle?,
    articleResultWrapper: ArticleResultWrapper?
): MergeData<TopArticle, ArticleResultWrapper>(topArticle, articleResultWrapper)