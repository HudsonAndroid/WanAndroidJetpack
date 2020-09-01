package com.hudson.wanandroid.ui.fragment.main

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.hudson.wanandroid.data.account.AccountRelative
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.wrapper.Status
import com.hudson.wanandroid.databinding.FragmentProjectsBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.ProjectsPagerAdapter
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.ProjectsModel
import javax.inject.Inject

class ProjectsFragment: Fragment(), Injectable, AccountRelative {
    companion object{
        const val CACHE_FRAGMENT_SIZE = 3
    }
    private var projectsBinding by autoCleared<FragmentProjectsBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val projectsModel:ProjectsModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        projectsBinding = FragmentProjectsBinding.inflate(inflater, container, false)

        return projectsBinding.root
    }

    override fun onAccountChanged(user: LoginUser?) {
        // 账号变动了，重新加载
        projectsModel.refresh()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectsBinding.category = projectsModel.projectCategory
        projectsBinding.lifecycleOwner = this
        projectsBinding.retry = object : RetryCallback{
            override fun retry() {
                projectsModel.retry()
            }
        }

        projectsModel.projectCategory.observe(viewLifecycleOwner, Observer {
            if(it.status == Status.SUCCESS && it.data != null){
                // set adapter
                projectsBinding.viewPager.adapter = ProjectsPagerAdapter(this, it.data)
                // change viewPager cache count, to cache more fragment
                projectsBinding.viewPager.offscreenPageLimit = CACHE_FRAGMENT_SIZE
                // attach
                TabLayoutMediator(projectsBinding.tabs, projectsBinding.viewPager){ tab, position ->
//            tab.setIcon()
                    var text: String? = null
                    if(position < it.data.size){
                        text = it.data[position].name
                    }
                    // tab title maybe contain html string
                    tab.text = text?.run { Html.fromHtml(this) }
                }.attach()
            }
        })
    }
}
