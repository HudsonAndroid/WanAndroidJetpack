package com.hudson.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.hudson.wanandroid.R
import com.just.agentweb.AgentWeb

class WebDetailActivity : AppCompatActivity() {
    private lateinit var agentWeb: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_detail)
        val container = findViewById<ConstraintLayout>(R.id.cl_container)
        val context = this
        // TODO: 2020/9/4 问题： 暗色模式下首次打开会白屏，原因是webView的构建过程中会把它附加到ViewGroup上
        // 由于内部逻辑被AgentWeb固定，无法先修改webView的theme再附加到ViewGroup上
        intent?.getStringExtra(KEY_URL)?.apply {
            agentWeb = AgentWeb.with(context)
                .setAgentWebParent(
                    container, ConstraintLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(this)
        }

        applyThemeIntoWebView()
    }

    private fun applyThemeIntoWebView(){
        // if we already set the night mode, we should apply it into webview which is code generated
        val isDarkTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES
//        val isSupportDark = WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)
        if(isDarkTheme && WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
            val settings = agentWeb.webCreator.webView.settings
            WebSettingsCompat.setForceDark(settings, WebSettingsCompat.FORCE_DARK_ON)
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if(agentWeb.handleKeyEvent(keyCode, event)){
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    companion object{
        const val KEY_URL = "url"

        fun start(from: Context, url: String){
            val intent = Intent(from, WebDetailActivity::class.java)
            intent.putExtra(KEY_URL, url)
            from.startActivity(intent)
        }
    }
}
