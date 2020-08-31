package com.hudson.wanandroid.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.databinding.FragmentBaseArticleBinding
import com.hudson.wanandroid.ui.adapter.ArticleAdapter
import com.hudson.wanandroid.ui.adapter.PagingLoadStateAdapter
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.util.autoCleared
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Hudson on 2020/8/26.
 */
abstract class ArticlePagerFragment: AccountRelativeFragment(){
    private var binding by autoCleared<FragmentBaseArticleBinding>()

    private val adapter: ArticleAdapter by lazy {
        ArticleAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.retry = object : RetryCallback{
            override fun retry() {
                adapter.retry()
            }
        }
        binding.pagingRetry = getPagingLoadState()
        binding.lifecycleOwner = this

        val retryLoad = PagingRetryLoad()
        adapter.addLoadStateListener { loadState ->
            if(adapter.itemCount > 0){
                // already has data list, ignore loading state
                retryLoad.hasShowData = true
            }else{
                retryLoad.loadStates = loadState.mediator
            }
            notifyLoadStateChange(retryLoad)
        }
        @OptIn(ExperimentalPagingApi::class)
        adapter.addDataRefreshListener {
            retryLoad.hasShowData = !it
            notifyLoadStateChange(retryLoad)
        }
    }

    final override fun onAccountChanged(user: LoginUser?) {
        if(binding.rvList.adapter == null){
            // not attach, so attach adapter
            binding.rvList.adapter = adapter.withLoadStateFooter(PagingLoadStateAdapter(adapter))
            lifecycleScope.launch {
                loadData().collectLatest {
                    adapter.submitData(it)
                }
            }
        }else{
            // already attached, reload from network
            adapter.refresh()
        }
    }

    abstract fun getPagingLoadState(): LiveData<PagingRetryLoad>?

    abstract fun notifyLoadStateChange(retryLoad: PagingRetryLoad)

    abstract fun loadData(): Flow<PagingData<Article>>
}