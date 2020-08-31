package com.hudson.wanandroid.ui.fragment.wechat

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.Subject
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.fragment.base.ArticlePagerFragment
import com.hudson.wanandroid.viewmodel.WechatItemModel
import javax.inject.Inject

/**
 * Created by Hudson on 2020/8/26.
 */
class WechatItemFragment(private val subject: Subject): ArticlePagerFragment(), Injectable{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val wechatItemModel: WechatItemModel by viewModels {
        viewModelFactory
    }

    override suspend fun starOrReverseArticle(article: Article)
            = wechatItemModel.starOrReverseArticle(requireActivity(), article)

    override fun getPagingLoadState() = wechatItemModel.loadState

    override fun notifyLoadStateChange(retryLoad: PagingRetryLoad) {
        wechatItemModel.loadState.value = retryLoad
    }

    override fun loadData() = wechatItemModel.loadData(subject.id, subject.parentChapterId)

}