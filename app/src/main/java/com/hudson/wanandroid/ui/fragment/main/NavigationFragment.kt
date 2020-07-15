package com.hudson.wanandroid.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import com.hudson.wanandroid.databinding.FragmentNavigationBinding

class NavigationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navigationBinding = FragmentNavigationBinding.inflate(inflater, container, false)
        val liveData1 = MutableLiveData<String>()
        liveData1.value = "额"
        val liveData2 = Transformations.map(liveData1) {
            it.length
        }
        liveData2.observe(this, Observer {
            Log.e("hudson","数据${it}")
        })
        navigationBinding.tvTest.setOnClickListener(View.OnClickListener {
            liveData1.value = "我是新的"
        })
        return navigationBinding.root
    }
}
