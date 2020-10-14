package com.hudson.wanandroid.ui.fragment.browser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.account.AccountRelative
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.UserWebsiteItem
import com.hudson.wanandroid.databinding.FragmentBrowserDefaultBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.activity.BrowserActivity
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.WebsiteModel
import com.hudson.wanandroid.viewmodel.bindingadapter.WebsiteClickListener
import javax.inject.Inject


class BrowserDefaultFragment : Fragment(), Injectable, AccountRelative{
    private var binding by autoCleared<FragmentBrowserDefaultBinding>()

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private val websiteModel: WebsiteModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBrowserDefaultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clickListener = object: WebsiteClickListener{
            override fun onWebsiteClick(userWebsiteItem: UserWebsiteItem) {
                val browserActivity = requireActivity() as BrowserActivity
                browserActivity.toggleInput(true)
                browserActivity.startSearch(userWebsiteItem.link)
            }
        }

        binding.lifecycleOwner = this
    }

    override fun onAccountChanged(user: LoginUser?) {
        user?.id?.run {
            binding.websites = websiteModel.loadCurrentUserWebsite(this)
        }
    }

}