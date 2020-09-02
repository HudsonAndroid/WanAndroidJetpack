package com.hudson.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.account.WanAndroidAccount
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.databinding.ActivityAccountManageBinding
import com.hudson.wanandroid.ui.adapter.AccountListAdapter
import com.hudson.wanandroid.ui.adapter.viewholder.OnUserItemClickListener
import kotlinx.coroutines.launch

class AccountManageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityAccountManageBinding>(
            this,
            R.layout.activity_account_manage
        )
        val wanAndroidAccount = WanAndroidAccount.getInstance()
        val userItemClickListener = object : OnUserItemClickListener{
            override fun onClick(loginUser: LoginUser) {
                if(!loginUser.current){
                    lifecycleScope.launch {
                        wanAndroidAccount.switchAccount(loginUser.id)
                    }
                }
            }
        }

        val accountListAdapter = AccountListAdapter(userItemClickListener)
        binding.rvAccount.adapter = accountListAdapter
        wanAndroidAccount.accountList().observe(this@AccountManageActivity, Observer {
            accountListAdapter.load(it)
        })
        binding.tvAdd.setOnClickListener {
            LoginActivity.start(this)
        }
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, AccountManageActivity::class.java))
        }
    }
}