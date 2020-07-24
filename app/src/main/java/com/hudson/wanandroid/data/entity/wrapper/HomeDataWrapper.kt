package com.hudson.wanandroid.data.entity.wrapper

import com.hudson.wanandroid.data.common.mergecall.MergeData
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.Banner
import com.hudson.wanandroid.data.entity.BannerItem

/**
 * 包含 Banner + TopArticle + HomeArticle
 * Created by Hudson on 2020/7/23.
 */
class HomeDataWrapper(
    articleWrapper: ArticleWrapper?,
    banner: Banner?
): MergeData<ArticleWrapper, Banner>(articleWrapper, banner)


class HomeData(
    var banners: List<BannerItem>?,
    var articles: List<Article>?
){
    override fun toString(): String {
        return "HomeData(banners=$banners, articles=$articles)"
    }

    fun isEmpty(): Boolean{
        return banners.isNullOrEmpty() && articles.isNullOrEmpty()
    }
}