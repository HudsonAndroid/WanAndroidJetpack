package com.hudson.wanandroid.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.hudson.wanandroid.data.entity.Subject
import com.hudson.wanandroid.data.entity.wrapper.Status
import com.hudson.wanandroid.databinding.FragmentDefaultTreeBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.activity.TreeListActivity
import com.hudson.wanandroid.ui.adapter.TreeListAdapter
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.TreeModel
import com.hudson.wanandroid.viewmodel.bindingadapter.TreeClickListener
import javax.inject.Inject


class DefaultTreeFragment : Fragment(), Injectable {
    private var binding by autoCleared<FragmentDefaultTreeBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val treeModel: TreeModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDefaultTreeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val clickListener = object: TreeClickListener {
            override fun onClick(targetTree: Subject, selectedIndex: Int) {
                // invoke while click the tree item
                TreeListActivity.start(view.context, Gson().toJson(targetTree), selectedIndex)
            }
        }
        val treeListAdapter = TreeListAdapter(clickListener)
        binding.rvList.adapter = treeListAdapter

        binding.trees = treeModel.treeList
        binding.lifecycleOwner = this
        binding.retry = object : RetryCallback{
            override fun retry() {
                treeModel.retry()
            }
        }

        treeModel.treeList.observe(viewLifecycleOwner, Observer {
            if(it.status == Status.SUCCESS && it.data != null){
                treeListAdapter.refresh(it.data)
            }
        })
    }

}