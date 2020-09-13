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
import com.hudson.wanandroid.databinding.FragmentUserScoreBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.PagingLoadStateAdapter
import com.hudson.wanandroid.ui.adapter.UserScoreAdapter
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.UserScoreModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserScoreFragment : Fragment(), Injectable, AccountRelative {
    private var binding by autoCleared<FragmentUserScoreBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val userScoreModel: UserScoreModel by viewModels {
        viewModelFactory
    }

    private val adapter: UserScoreAdapter by lazy {
        UserScoreAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAccountChanged(user: LoginUser?) {
        if(binding.rvList.adapter == null){
            // not attach, so attach adapter
            binding.rvList.itemAnimator = null
            binding.rvList.adapter = adapter.withLoadStateFooter(PagingLoadStateAdapter(adapter))
            lifecycleScope.launch {
                userScoreModel.loadUserScoreRank().collectLatest {
                    adapter.submitData(it)
                }
            }
        }else{
            // already attached, reload from network
            adapter.refresh()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.retry = object : RetryCallback {
            override fun retry() {
                adapter.retry()
            }
        }
        binding.pagingRetry = userScoreModel.userScoreRankLoadState
        binding.lifecycleOwner = this

        val retryLoad = PagingRetryLoad()
        adapter.addLoadStateListener { loadState ->
            if(adapter.itemCount > 0){
                // already has data list, ignore loading state
                retryLoad.hasShowData = true
            }else{
                retryLoad.loadStates = loadState.mediator
            }
            userScoreModel.userScoreRankLoadState.value = retryLoad
        }
        @OptIn(ExperimentalPagingApi::class)
        adapter.addDataRefreshListener {
            retryLoad.hasShowData = !it
            userScoreModel.userScoreRankLoadState.value = retryLoad
        }
    }

}
