package com.hudson.wanandroid.ui.fragment.tree

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.Subject
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.fragment.base.ArticlePagerFragment
import com.hudson.wanandroid.viewmodel.TreeItemModel
import javax.inject.Inject

/**
 * Created by Hudson on 2020/8/26.
 */
class TreeArticleFragment(private val subject: Subject) : ArticlePagerFragment(), Injectable{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val treeItemModel: TreeItemModel by viewModels {
        viewModelFactory
    }

    override fun getPagingLoadState() = treeItemModel.loadState

    override fun notifyLoadStateChange(retryLoad: PagingRetryLoad) {
        treeItemModel.loadState.value = retryLoad
    }

    override fun loadData() = treeItemModel.loadArticles(subject.id, subject.parentChapterId)

}