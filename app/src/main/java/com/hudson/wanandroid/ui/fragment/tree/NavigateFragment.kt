package com.hudson.wanandroid.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.entity.wrapper.Status
import com.hudson.wanandroid.databinding.FragmentTreeNavigationBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.NavigationListAdapter
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.NavigationModel
import javax.inject.Inject


class NavigateFragment : Fragment(), Injectable {
    private var binding by autoCleared<FragmentTreeNavigationBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val navigationModel: NavigationModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTreeNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navigationListAdapter = NavigationListAdapter()

        binding.rvList.adapter = navigationListAdapter

        binding.result = navigationModel.navigationList
        binding.lifecycleOwner = this
        binding.retry = object : RetryCallback{
            override fun retry() {
                navigationModel.retry()
            }
        }

        navigationModel.navigationList.observe(viewLifecycleOwner, Observer {
            if(it.status == Status.SUCCESS && it.data != null){
                navigationListAdapter.refresh(it.data)
            }
        })
    }

}