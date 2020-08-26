package com.hudson.wanandroid.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hudson.wanandroid.databinding.FragmentWechatBinding

/**
 * 公众号
 */
class WechatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navigationBinding = FragmentWechatBinding.inflate(inflater, container, false)
        return navigationBinding.root
    }
}
