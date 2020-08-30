package com.hudson.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hudson.wanandroid.R
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), HasSupportFragmentInjector {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}
