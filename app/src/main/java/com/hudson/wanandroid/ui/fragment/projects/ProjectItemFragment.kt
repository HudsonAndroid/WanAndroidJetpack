package com.hudson.wanandroid.ui.fragment.projects

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.ProjectInfo
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.fragment.base.ArticlePagerFragment
import com.hudson.wanandroid.viewmodel.ProjectItemModel
import javax.inject.Inject

private const val ARG_PROJECT_ID = "projectId"
/**
 * 发现个问题，多个fragment使用同一个数据表，例如这里的article，
 * 某个Paging会偶发性触发另一个paging数据，并一直显示加载状态，
 * 因此根据加载状态前adapter.itemCount值来关闭加载进度条
 */
class ProjectItemFragment: ArticlePagerFragment(), Injectable {
    private var projectId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectId = it.getInt(ARG_PROJECT_ID)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val projectItemModel: ProjectItemModel by viewModels {
        viewModelFactory
    }

    override suspend fun starOrReverseArticle(article: Article)
            = projectItemModel.starOrReverseArticle(requireActivity(), article)

    override fun getPagingLoadState() = projectItemModel.loadState

    override fun notifyLoadStateChange(retryLoad: PagingRetryLoad) {
        projectItemModel.loadState.value = retryLoad
    }

    override fun loadData() = projectItemModel.loadData(projectId!!)


    companion object{
        // don't use fragment constructor with params, it's maybe make your application crash
        // keep a static function for system call constructor, such as day-night mode switch
        // more information see:
        // https://stackoverflow.com/questions/51831053/could-not-find-fragment-constructor
        @JvmStatic
        fun newInstance(projectInfo: ProjectInfo) =
            ProjectItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PROJECT_ID, projectInfo.id)
                }
            }
    }

}