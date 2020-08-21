package com.hudson.wanandroid.ui.fragment.projects

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.LongSparseArray
import androidx.core.util.contains
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.ProjectInfo
import com.hudson.wanandroid.data.repository.ProjectsRepository
import com.hudson.wanandroid.databinding.FragmentProjectItemBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.ArticleAdapter
import com.hudson.wanandroid.ui.adapter.PagingLoadStateAdapter
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.ProjectItemModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 发现个问题，多个fragment使用同一个数据表，例如这里的article，
 * 某个Paging会偶发性触发另一个paging数据，并一直显示加载状态，
 * 因此根据加载状态前adapter.itemCount值来关闭加载进度条
 */
class ProjectItemFragment(private val projectInfo: ProjectInfo) : Fragment(), Injectable {
    private var binding by autoCleared<FragmentProjectItemBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val projectItemModel: ProjectItemModel by viewModels {
        viewModelFactory
    }

    private val adapter: ArticleAdapter by lazy {
        ArticleAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProjectItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.retry = object : RetryCallback {
            override fun retry() {
                adapter.retry()
            }
        }
        binding.pagingRetry = projectItemModel.loadState
        binding.lifecycleOwner = this

        val footerLoadStateAdapter = PagingLoadStateAdapter(adapter)
        binding.rvList.adapter = adapter.withLoadStateFooter(footerLoadStateAdapter)

        val retryLoad = PagingRetryLoad()
        adapter.addLoadStateListener { loadState ->
            if(adapter.itemCount > 0){
                // already has data list, ignore loading state
                retryLoad.hasShowData = true
            }else{
                retryLoad.loadStates = loadState.mediator
            }
            projectItemModel.loadState.value = retryLoad
        }
        @OptIn(ExperimentalPagingApi::class)
        adapter.addDataRefreshListener {
            retryLoad.hasShowData = !it
            projectItemModel.loadState.value = retryLoad
        }
        lifecycleScope.launch {
            projectItemModel.loadData(projectInfo.id).collectLatest {
                adapter.submitData(it)
            }
        }
    }

}