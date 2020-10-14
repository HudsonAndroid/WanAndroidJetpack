package com.hudson.wanandroid.ui.fragment.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.common.WanAndroidConfig
import com.hudson.wanandroid.data.common.fromJson
import com.hudson.wanandroid.data.entity.baiduEngine
import com.hudson.wanandroid.databinding.FragmentDefaultSettingBinding
import com.hudson.wanandroid.databinding.FragmentSearchDefaultBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.SearchModel
import com.hudson.wanandroid.viewmodel.SettingModel
import javax.inject.Inject

class DefaultSettingFragment : Fragment(), Injectable {
    private var binding by autoCleared<FragmentDefaultSettingBinding>()

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private val settingModel: SettingModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDefaultSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.engine = settingModel.searchEngine
        val string = WanAndroidConfig(requireContext()).getString(
            WanAndroidConfig.COMMON_BROWSER_SEARCH_TOOL,
            null
        )
        val engine = if(string != null) Gson().fromJson(string) else baiduEngine
        settingModel.searchEngine.value = engine
        binding.lifecycleOwner = this
        binding.tvBrowserContent.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(DefaultSettingFragmentDirections
                    .actionDefaultSettingFragmentToSearchSelectFragment())
        }
    }

}