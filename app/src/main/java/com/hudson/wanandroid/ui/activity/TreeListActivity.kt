package com.hudson.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.common.fromJson
import com.hudson.wanandroid.data.entity.TreeItem
import com.hudson.wanandroid.databinding.ActivityTreeListBinding
import com.hudson.wanandroid.ui.adapter.TreeListPagerAdapter
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import java.lang.IllegalArgumentException
import javax.inject.Inject

class TreeListActivity : FragmentActivity(), HasSupportFragmentInjector {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(intent != null){
            val treeItemStr = intent.getStringExtra(KEY_TREE_LIST)
            try {
                val treeItem: TreeItem = Gson().fromJson(treeItemStr)
                val trees = treeItem.children
                val selectedIndex = intent.getIntExtra(KEY_SELECT_INDEX, -1)
                if(selectedIndex < 0 || selectedIndex >= trees.size){
                    throw IllegalArgumentException("the input param is invalid: $selectedIndex, must in 0 ~ ${trees.size}")
                }
                val binding = DataBindingUtil.setContentView<ActivityTreeListBinding>(
                    this,
                    R.layout.activity_tree_list
                )
                binding.viewPager.adapter = TreeListPagerAdapter(this, trees)
                binding.viewPager.currentItem = selectedIndex
                binding.tvTitle.text = treeItem.name
                TabLayoutMediator(binding.tabs, binding.viewPager){ tab, position ->
                    val text = trees[position].name
                    tab.text = text.run{ Html.fromHtml(text) }
                }.attach()
            }catch (e: Exception){
                e.printStackTrace()
                Timber.e(getString(R.string.error_invalid_params))
                finish()
            }
        }
    }

    companion object{
        private const val KEY_TREE_LIST = "treeList"
        private const val KEY_SELECT_INDEX = "selectIndex"

        fun start(context: Context, treeList: String, selectedIndex: Int){
            val intent = Intent(context, TreeListActivity::class.java)
            intent.putExtra(KEY_TREE_LIST, treeList)
            intent.putExtra(KEY_SELECT_INDEX, selectedIndex)
            context.startActivity(intent)
        }
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}