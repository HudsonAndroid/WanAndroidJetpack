package com.hudson.wanandroid.ui.fragment.browser

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.webkit.URLUtil
import android.webkit.WebView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.google.gson.Gson
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.common.WanAndroidConfig
import com.hudson.wanandroid.data.common.fromJson
import com.hudson.wanandroid.data.entity.SearchEngine
import com.hudson.wanandroid.data.entity.baiduEngine
import com.hudson.wanandroid.databinding.FragmentBrowserMainBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.activity.BrowserActivity
import com.hudson.wanandroid.ui.util.showToast
import com.hudson.wanandroid.viewmodel.WebsiteModel
import com.just.agentweb.AgentWeb
import com.just.agentweb.WebViewClient
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


class BrowserMainFragment : Fragment(), Injectable {
    private lateinit var agentWeb: AgentWeb
    private lateinit var binding: FragmentBrowserMainBinding

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    private val websiteModel: WebsiteModel by activityViewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBrowserMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        agentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                binding.clContainer, ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator()
            .setWebViewClient(webViewClient)
            .createAgentWeb()
            .ready()
            .get()

        applyThemeIntoWebView()

        configBackPress()

        websiteModel.searchUrl.observe(viewLifecycleOwner, Observer {
            val urlLoader = agentWeb.urlLoader
            if(isWebLink(it)){
                urlLoader.loadUrl(it)
            }else{
                val json = WanAndroidConfig(requireContext())
                    .getString(WanAndroidConfig.COMMON_BROWSER_SEARCH_TOOL, null)
                val searchEngine = if(json != null) Gson().fromJson(json) else baiduEngine
                urlLoader.loadUrl(
                    searchEngine.link
                        + it)
            }
        })
    }

    private fun isWebLink(url: String) = URLUtil.isValidUrl(url)

    private fun configBackPress(){
        val activity = requireActivity() as BrowserActivity
        activity.onBackPressedDispatcher.addCallback(activity, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val navController = activity.findNavController(R.id.nav_host_fragment)
                if(navController.graph.startDestination == navController.currentDestination?.id){
                    // 需要单独处理，因为如果混在一起的话，实际第二个fragment的webView已经处于destroyed状态
                    activity.finish()
                }else if(!agentWeb.handleKeyEvent(KeyEvent.KEYCODE_BACK, null)){
                    navController.navigateUp()
                    activity.backInitialState(true)
                }
            }
        })
        activity.bindStarListener(View.OnClickListener {
            lifecycleScope.launch {
                val webView = agentWeb.webCreator.webView
                val starWebsite = websiteModel.starWebsite(webView.title, webView.url)
                if(starWebsite.isSuccess()){
                    showToast(R.string.tips_star_success)
                }else{
                    showToast(R.string.tips_star_failed)
                }
            }
        })
    }

    private fun applyThemeIntoWebView(){
        // if we already set the night mode, we should apply it into webview which is code generated
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES
//        val isSupportDark = WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)
        if(isDarkTheme && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
            // fix first open show blank page bug
            agentWeb.webCreator.webView.setBackgroundColor(Color.BLACK)
            val settings = agentWeb.webCreator.webView.settings
            WebSettingsCompat.setForceDark(settings, WebSettingsCompat.FORCE_DARK_ON)
        }
    }

    private val webViewClient: WebViewClient = object : WebViewClient(){
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            checkUrlStared(url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            checkUrlStared(url)
        }
    }

    private fun checkUrlStared(url: String?) {
        url?.run {
            lifecycleScope.launch {
                websiteModel.checkUrlStared(url)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        agentWeb.webLifeCycle.onPause()
    }

    override fun onResume() {
        super.onResume()
        agentWeb.webLifeCycle.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        agentWeb.webLifeCycle.onDestroy()
    }
}