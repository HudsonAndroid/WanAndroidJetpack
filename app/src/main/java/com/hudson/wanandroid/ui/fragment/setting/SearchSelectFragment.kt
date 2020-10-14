package com.hudson.wanandroid.ui.fragment.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.common.WanAndroidConfig
import com.hudson.wanandroid.data.common.fromJson
import com.hudson.wanandroid.data.entity.SearchEngine
import com.hudson.wanandroid.data.entity.baiduEngine
import com.hudson.wanandroid.data.entity.googleEngine
import com.hudson.wanandroid.databinding.FragmentDefaultSettingBinding
import com.hudson.wanandroid.databinding.FragmentSearchDefaultBinding
import com.hudson.wanandroid.databinding.FragmentSearchSelectBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.SearchEngineAdapter
import com.hudson.wanandroid.ui.adapter.viewholder.OnSearchEngineClickListener
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.SettingModel
import javax.inject.Inject

class SearchSelectFragment : Fragment(), Injectable {
    private var binding by autoCleared<FragmentSearchSelectBinding>()

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private val settingModel: SettingModel by activityViewModels {
        viewModelFactory
    }

    var oldEngine: SearchEngine? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var searchEngineAdapter: SearchEngineAdapter? = null
        val searchEngineClickListener = object: OnSearchEngineClickListener{
            override fun onClick(engine: SearchEngine) {
                oldEngine?.current = false
                engine.current = true
                settingModel.searchEngine.value = engine
                searchEngineAdapter?.notifyDataSetChanged()
            }
        }
        searchEngineAdapter = SearchEngineAdapter(searchEngineClickListener)
        binding.rvSearchEngine.adapter = searchEngineAdapter
        binding.lifecycleOwner = this

        searchEngineAdapter.refresh(engines)

        val string = WanAndroidConfig(requireContext()).getString(
            WanAndroidConfig.COMMON_BROWSER_SEARCH_TOOL,
            null
        )
        oldEngine = if(string != null) Gson().fromJson(string) else baiduEngine
        for (engine in engines) {
            if(engine.link == oldEngine?.link){
                engine.current = true
                oldEngine = engine
            }
        }
    }

    companion object{
        val engines = mutableListOf(baiduEngine, googleEngine)
    }
}