package com.hudson.wanandroid.ui.fragment.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.hudson.wanandroid.databinding.FragmentSearchDefaultBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.viewmodel.SearchModel
import javax.inject.Inject


class SearchDefaultFragment : Fragment(), Injectable {
    private var binding by autoCleared<FragmentSearchDefaultBinding>()

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private val searchModel: SearchModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchDefaultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hotWords = searchModel.hotSearchWord
        binding.historyWords = searchModel.historySearch
        binding.clickListener = View.OnClickListener {
            if(it is TextView){
                // jump to search result fragment with params
                // 在SearchResultFragment中设置了需要传递的参数
                val queryWord = it.text.toString()
                val action =
                    SearchDefaultFragmentDirections
                        .actionSearchDefaultFragmentToSearchResultFragment(queryWord)
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
        binding.cleanListener = searchModel.onCleanClick
        binding.lifecycleOwner = this
    }

}