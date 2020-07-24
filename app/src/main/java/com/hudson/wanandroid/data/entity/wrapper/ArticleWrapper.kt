package com.hudson.wanandroid.data.entity.wrapper

import com.hudson.wanandroid.data.common.mergecall.MergeData
import com.hudson.wanandroid.data.entity.HomeArticle
import com.hudson.wanandroid.data.entity.TopArticle

/**
 * Created by Hudson on 2020/7/22.
 */
class ArticleWrapper(
    topArticle: TopArticle?,
    homeArticle: HomeArticle?
): MergeData<TopArticle, HomeArticle>(topArticle, homeArticle)