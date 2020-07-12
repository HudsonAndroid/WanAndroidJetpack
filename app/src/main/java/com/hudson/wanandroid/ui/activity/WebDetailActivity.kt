package com.hudson.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.hudson.wanandroid.R

class WebDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_detail)
        val container = findViewById<ConstraintLayout>(R.id.cl_container)
        val webView = WebView(this.applicationContext)
        container.addView(webView)
        intent?.getStringExtra(KEY_URL)?.apply {
            webView.loadUrl(this)
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
