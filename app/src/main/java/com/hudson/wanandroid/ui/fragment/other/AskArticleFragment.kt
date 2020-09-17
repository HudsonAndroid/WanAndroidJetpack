package com.hudson.wanandroid.ui.fragment.other

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.fragment.base.ArticlePagerFragment
import com.hudson.wanandroid.viewmodel.AskArticleModel
import javax.inject.Inject

class AskArticleFragment : ArticlePagerFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val askArticleModel: AskArticleModel by viewModels {
        viewModelFactory
    }

    override suspend fun starOrReverseArticle(article: Article)
        = askArticleModel.starOrReverseArticle(requireActivity(), article)

    override fun getPagingLoadState() = askArticleModel.loadState

    override fun notifyLoadStateChange(retryLoad: PagingRetryLoad) {
        askArticleModel.loadState.value = retryLoad
    }

    override fun loadData()
        = askArticleModel.loadAskArticles()

}