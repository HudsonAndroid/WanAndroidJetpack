package com.hudson.wanandroid.ui.fragment.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.databinding.FragmentCommonPagingBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.ArticleAdapter
import com.hudson.wanandroid.ui.adapter.PagingLoadStateAdapter
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.SearchModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchResultFragment : Fragment(), Injectable {
    private var binding by autoCleared<FragmentCommonPagingBinding>()
    private lateinit var retryLoad: PagingRetryLoad

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // ViewModel在Activity范围内共享
    private val searchModel: SearchModel by activityViewModels {
        viewModelFactory
    }

    private val articleAdapter: ArticleAdapter by lazy {
        ArticleAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommonPagingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 获取搜索参数
        val searchWord = SearchResultFragmentArgs.fromBundle(requireArguments()).searchWord

        binding.lifecycleOwner = this
        binding.pagingRetry = searchModel.searchLoadState
        binding.retry = object : RetryCallback {
            override fun retry() {
                val lastFailedSearch = searchModel.searchWord.value
                searchModel.searchWord.value = lastFailedSearch
            }
        }

        searchModel.searchResult.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                articleAdapter.submitData(it)
            }
        })

        searchModel.searchWord.value = searchWord

        binding.rvList.adapter = articleAdapter.withLoadStateFooter(
            PagingLoadStateAdapter(articleAdapter)
        )
        retryLoad = PagingRetryLoad()
        articleAdapter.addLoadStateListener {
            retryLoad.loadStates = it.source // only source available
            searchModel.searchLoadState.value = retryLoad
        }
        @OptIn(ExperimentalPagingApi::class)
        articleAdapter.addDataRefreshListener {
            retryLoad.hasShowData = !it
            searchModel.searchLoadState.value = retryLoad
        }
    }

}