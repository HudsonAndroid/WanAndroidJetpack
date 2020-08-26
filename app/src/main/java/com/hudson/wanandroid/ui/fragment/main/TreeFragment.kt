package com.hudson.wanandroid.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.hudson.wanandroid.R
import com.hudson.wanandroid.databinding.FragmentTreeBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.DEFAULT_TREE_INDEX
import com.hudson.wanandroid.ui.adapter.NAVIGATION_INDEX
import com.hudson.wanandroid.ui.adapter.TreePagerAdapter
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.TreeModel
import com.hudson.wanandroid.viewmodel.provider.ViewModelFactory_Factory
import javax.inject.Inject

class TreeFragment : Fragment() {
    private var binding by autoCleared<FragmentTreeBinding>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = TreePagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }

    private fun getTabTitle(position: Int): String? {
        return when(position){
            DEFAULT_TREE_INDEX -> getString(R.string.fragment_tree)
            NAVIGATION_INDEX -> getString(R.string.fragment_nav)
            else -> null
        }
    }
}
