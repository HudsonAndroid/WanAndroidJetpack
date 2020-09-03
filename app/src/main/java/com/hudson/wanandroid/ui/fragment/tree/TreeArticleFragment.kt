package com.hudson.wanandroid.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.Subject
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.fragment.base.ArticlePagerFragment
import com.hudson.wanandroid.viewmodel.TreeItemModel
import javax.inject.Inject

private const val ARG_SUBJECT = "subject"

/**
 * Created by Hudson on 2020/8/26.
 */
class TreeArticleFragment: ArticlePagerFragment(), Injectable{
    lateinit var subject: Subject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subject = it.getSerializable(ARG_SUBJECT) as Subject
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val treeItemModel: TreeItemModel by viewModels {
        viewModelFactory
    }

    override suspend fun starOrReverseArticle(article: Article)
            = treeItemModel.starOrReverseArticle(requireActivity(), article)

    override fun getPagingLoadState() = treeItemModel.loadState

    override fun notifyLoadStateChange(retryLoad: PagingRetryLoad) {
        treeItemModel.loadState.value = retryLoad
    }

    override fun loadData() = treeItemModel.loadArticles(subject.id, subject.parentChapterId)


    companion object{

        // keep fragment constructor without param
        @JvmStatic
        fun newInstance(subject: Subject) =
            TreeArticleFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SUBJECT, subject)
                }
            }
    }
}