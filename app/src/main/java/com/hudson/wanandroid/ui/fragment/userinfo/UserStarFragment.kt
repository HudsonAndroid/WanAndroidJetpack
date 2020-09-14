package com.hudson.wanandroid.ui.fragment.userinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.hudson.wanandroid.data.account.AccountRelative
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.StarArticle
import com.hudson.wanandroid.databinding.FragmentCommonPagingBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.PagingLoadStateAdapter
import com.hudson.wanandroid.ui.adapter.StarArticleAdapter
import com.hudson.wanandroid.ui.adapter.viewholder.StarClickListener
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.StarModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserStarFragment : Fragment(), Injectable, AccountRelative {

    private var binding by autoCleared<FragmentCommonPagingBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val starModel: StarModel by viewModels {
        viewModelFactory
    }

    private val starClickListener = object: StarClickListener {
        override fun onStarClick(article: StarArticle, position: Int) {
            lifecycleScope.launch {
                starModel.unStarArticle(requireActivity(), article)
                // 如果是收藏，出现问题是偶发性不会刷新收藏状态，因此手动通知刷新
                // 关联问题见：
                // https://stackoverflow.com/questions/51889154/recycler-view-not-scrolling-to-the-top-after-adding-new-item-at-the-top-as-chan
                // https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
                adapter.notifyItemChanged(position)
            }
        }
    }

    private val adapter: StarArticleAdapter by lazy {
        StarArticleAdapter(starClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommonPagingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.retry = object : RetryCallback {
            override fun retry() {
                adapter.retry()
            }
        }
        binding.pagingRetry = starModel.loadState
        binding.lifecycleOwner = this

        val retryLoad = PagingRetryLoad()
        adapter.addLoadStateListener { loadState ->
            if(adapter.itemCount > 0){
                // already has data list, ignore loading state
                retryLoad.hasShowData = true
            }else{
                retryLoad.loadStates = loadState.mediator
            }
            starModel.loadState.value = retryLoad
        }
        @OptIn(ExperimentalPagingApi::class)
        adapter.addDataRefreshListener {
            retryLoad.hasShowData = !it
            starModel.loadState.value = retryLoad
        }
    }

    override fun onAccountChanged(user: LoginUser?) {
        if(binding.rvList.adapter == null){
            // not attach, so attach adapter
            binding.rvList.itemAnimator = null
            binding.rvList.adapter = adapter.withLoadStateFooter(PagingLoadStateAdapter(adapter))
            lifecycleScope.launch {
                starModel.loadStarArticles().collectLatest {
                    adapter.submitData(it)
                }
            }
        }else{
            // already attached, reload from network
            adapter.refresh()
        }
    }
}
