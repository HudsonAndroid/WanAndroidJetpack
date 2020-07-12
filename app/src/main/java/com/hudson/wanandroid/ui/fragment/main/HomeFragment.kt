package com.hudson.wanandroid.ui.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.hudson.wanandroid.databinding.PageHomeBinding

/**
 * Created by Hudson on 2020/7/12.
 */
class HomeFragment: Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        PageHomeBinding.inflate(LayoutInflater.from(view.context))
    }
}