package com.hudson.wanandroid.ui.fragment.other

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.fragment.base.ArticlePagerFragment
import com.hudson.wanandroid.viewmodel.SquareArticleModel
import javax.inject.Inject

class SquareArticleFragment : ArticlePagerFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val squareArticleModel: SquareArticleModel by viewModels {
        viewModelFactory
    }

    override suspend fun starOrReverseArticle(article: Article)
            = squareArticleModel.starOrReverseArticle(requireActivity(), article)

    override fun getPagingLoadState() = squareArticleModel.loadState

    override fun notifyLoadStateChange(retryLoad: PagingRetryLoad) {
        squareArticleModel.loadState.value = retryLoad
    }

    override fun loadData()
            = squareArticleModel.loadSquareArticles()
}