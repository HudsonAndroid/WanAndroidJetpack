package com.hudson.wanandroid.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.hudson.wanandroid.R
import com.hudson.wanandroid.databinding.FragmentHomeBinding
import com.hudson.wanandroid.databinding.FragmentProjectsBinding
import com.hudson.wanandroid.ui.util.autoCleared

class ProjectsFragment : Fragment() {
    private var projectsBinding by autoCleared<FragmentProjectsBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        projectsBinding = FragmentProjectsBinding.inflate(inflater, container, false)

        TabLayoutMediator(projectsBinding.tabs, projectsBinding.viewPager){ tab, position ->
//            tab.setIcon()

        }.attach()

        return projectsBinding.root
    }
}
