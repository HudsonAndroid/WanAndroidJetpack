package com.hudson.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.entity.SearchEngine
import javax.inject.Inject

/**
 * Created by Hudson on 2020/9/25.
 */
class SettingModel @Inject constructor(): ViewModel(){

    val searchEngine = MutableLiveData<SearchEngine>()
}