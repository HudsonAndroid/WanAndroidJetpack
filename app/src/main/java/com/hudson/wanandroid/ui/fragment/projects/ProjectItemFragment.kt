package com.hudson.wanandroid.ui.fragment.projects

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.ProjectInfo
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.fragment.base.ArticlePagerFragment
import com.hudson.wanandroid.viewmodel.ProjectItemModel
import javax.inject.Inject

/**
 * 发现个问题，多个fragment使用同一个数据表，例如这里的article，
 * 某个Paging会偶发性触发另一个paging数据，并一直显示加载状态，
 * 因此根据加载状态前adapter.itemCount值来关闭加载进度条
 */
class ProjectItemFragment(private val projectInfo: ProjectInfo) : ArticlePagerFragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val projectItemModel: ProjectItemModel by viewModels {
        viewModelFactory
    }

    override fun getPagingLoadState() = projectItemModel.loadState

    override fun notifyLoadStateChange(retryLoad: PagingRetryLoad) {
        projectItemModel.loadState.value = retryLoad
    }

    override fun loadData() = projectItemModel.loadData(projectInfo.id)

}