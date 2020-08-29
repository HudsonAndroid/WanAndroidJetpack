package com.hudson.wanandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.repository.WechatRepository
import javax.inject.Inject

/**
 * Created by Hudson on 2020/8/26.
 */
class WechatModel @Inject constructor(private val repository: WechatRepository): ViewModel() {

    val wechatCategory = repository.loadWechatCategory()

    fun retry() = repository.retry()

    fun refresh() = repository.refresh()
}