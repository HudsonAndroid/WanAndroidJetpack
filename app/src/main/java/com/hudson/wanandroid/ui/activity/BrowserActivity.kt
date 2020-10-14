package com.hudson.wanandroid.ui.activity

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hudson.wanandroid.R
import com.hudson.wanandroid.databinding.ActivityBrowserBinding
import com.hudson.wanandroid.ui.fragment.browser.BrowserDefaultFragmentDirections
import com.hudson.wanandroid.ui.view.OnDismissKeyboardListener
import com.hudson.wanandroid.viewmodel.WebsiteModel
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

class BrowserActivity : AppCompatActivity(), HasSupportFragmentInjector {
    private lateinit var binding: ActivityBrowserBinding

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private val websiteModel: WebsiteModel by viewModels {
        viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView<ActivityBrowserBinding>(this, R.layout.activity_browser)

        binding.ivBack.setOnClickListener{
            handleBackInvoke()
        }
        binding.ivSearch.setOnClickListener{
            hideSoftInput()
            startSearch(binding.etInput.text?.toString())
        }
        binding.etInput.setOnEditorActionListener { view, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                hideSoftInput()
                startSearch(binding.etInput.text?.toString())
                true
            }else false
        }
        binding.etInput.setOnFocusChangeListener { v, hasFocus ->
            toggleInput(hasFocus)
        }
        binding.etInput.dismissKeyboardListener = object: OnDismissKeyboardListener{
            override fun onDismissKeyboard() = backInitialState()
        }
        binding.searchUrl = websiteModel.searchUrl
        binding.stared = websiteModel.isStared
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
    }

    fun backInitialState(fromSecondPage: Boolean = false): Boolean{
        val navController = getNavController()
        val tag = binding.etInput.tag
        val flag = tag?.let {
            if(it is Boolean) it else false
        }
        if(navController.graph.startDestination == navController.currentDestination?.id
            && flag == false){
            // 关闭输入法的同时，需要把输入框回移
            hideSoftInput()
            binding.etInput.setText("")
            binding.etInput.clearFocus()
            binding.ivBack.visibility = GONE
            binding.ivStar.visibility = GONE
            toggleInput(false)
            return true
        }
        return false
    }

    fun toggleInput(hasTarget: Boolean){
        var isCenter = false
        val layoutParams = binding.etInput.layoutParams as ConstraintLayout.LayoutParams
        val start = layoutParams.verticalBias
        val end = if(hasTarget){
            isCenter = false
            0f
        }else{
            isCenter = true
            0.333f
        }
        val animator = ValueAnimator.ofFloat(start, end)
        animator.duration = 200
        animator.addUpdateListener {
            val params = binding.etInput.layoutParams as ConstraintLayout.LayoutParams
            params.verticalBias = it.animatedValue as Float
            binding.etInput.layoutParams = params
        }
        animator.doOnEnd {
            binding.etInput.tag = isCenter
        }
        animator.start()
    }

    private fun hideSoftInput(){
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun bindStarListener(listener: View.OnClickListener){
        binding.ivStar.setOnClickListener(listener)
    }

    fun startSearch(queryWord: String?) {
        if (!queryWord.isNullOrBlank()) {
            val navController = getNavController()
            // if current is first page, we should navigate to result fragment
            if (navController.graph.startDestination == navController.currentDestination?.id) {
                val action =
                    BrowserDefaultFragmentDirections
                        .actionBrowserDefaultFragmentToBrowserMainFragment()

                getNavController().navigate(action)
                binding.ivBack.visibility = VISIBLE
                binding.ivStar.visibility = VISIBLE
            }
            // start search
            websiteModel.searchUrl.value = queryWord
        }
    }

    private fun getNavController() = findNavController(R.id.nav_host_fragment)

    private fun handleBackInvoke(){
        if(!getNavController().navigateUp()){
            finish()
        }
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, BrowserActivity::class.java))
        }
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}