package com.hudson.wanandroid.ui.fragment.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hudson.wanandroid.data.account.AccountRelative
import com.hudson.wanandroid.data.account.WanAndroidAccount

/**
 * Created by Hudson on 2020/8/29.
 */
abstract class AccountRelativeFragment : Fragment(), AccountRelative{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 监听账号变动
        WanAndroidAccount.getInstance().currentUser.observe(viewLifecycleOwner, Observer {
            onAccountChanged(it)
        })
    }

}