package com.hudson.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.hudson.wanandroid.R
import com.just.agentweb.AgentWeb

class WebDetailActivity : AppCompatActivity() {
    private lateinit var agentWeb: AgentWeb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_detail)
        val container = findViewById<ConstraintLayout>(R.id.cl_container)
        val context = this
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
