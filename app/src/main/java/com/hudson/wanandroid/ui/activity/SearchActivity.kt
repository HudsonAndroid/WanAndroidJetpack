package com.hudson.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.work.WorkManager
import com.hudson.wanandroid.R
import com.hudson.wanandroid.databinding.ActivitySearchBinding
import com.hudson.wanandroid.ui.fragment.search.SearchDefaultFragmentDirections
import com.hudson.wanandroid.viewmodel.SearchModel
import com.hudson.wanandroid.works.createPeriodicRequest
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class SearchActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private val searchModel: SearchModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)
        binding.ivBack.setOnClickListener{
            handleBackInvoke()
        }
        binding.ivSearch.setOnClickListener{
            startSearch(binding)
        }
        binding.etInput.setOnEditorActionListener { view, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                hideSoftInput()
                startSearch(binding)
                true
            }else false
        }
        binding.searchWord = searchModel.searchWord
        binding.lifecycleOwner = this

        // custom handle back press event
        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                handleBackInvoke()
            }
        }
        // 可以不需要移除，因为在LifecycleOwner移除的时候，会自动处理remove
        // https://developer.android.google.cn/guide/navigation/navigation-custom-back
        onBackPressedDispatcher.addCallback(this, callback)


        // 定时清理多余的搜索历史
        WorkManager.getInstance(this).enqueue(createPeriodicRequest())
    }


    private fun hideSoftInput(){
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun startSearch(binding: ActivitySearchBinding) {
        val queryWord = binding.etInput.text?.toString()
        if (!queryWord.isNullOrBlank()) {
            val navController = getNavController()
            // if current is first page, we should navigate to result fragment
            if (navController.graph.startDestination == navController.currentDestination?.id) {
                // jump to search result fragment with params
                // 在SearchResultFragment中设置了需要传递的参数
                val action =
                    SearchDefaultFragmentDirections
                        .actionSearchDefaultFragmentToSearchResultFragment(queryWord)

                getNavController().navigate(action)
            }
            // start search
            searchModel.searchWord.value = queryWord
        }
    }

    private fun getNavController() = findNavController(R.id.nav_host_fragment)

    private fun handleBackInvoke(){
        if(!getNavController().navigateUp()){
            finish()
        }
    }

    companion object{

        fun start(from: Context){
            from.startActivity(Intent(from, SearchActivity::class.java))
        }
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}