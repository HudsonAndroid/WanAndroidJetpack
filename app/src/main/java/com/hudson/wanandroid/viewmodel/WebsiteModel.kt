package com.hudson.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.repository.WebsiteRepository
import javax.inject.Inject

/**
 * Created by Hudson on 2020/9/18.
 */
class WebsiteModel @Inject constructor(private val repository: WebsiteRepository): ViewModel() {

    fun loadCurrentUserWebsite(userId: Long) = repository.loadUserWebsite(userId)

    suspend fun starWebsite(name: String, link: String) = repository.starWebsite(name, link)

    val searchUrl = MutableLiveData<String>()

    val isStared = MutableLiveData<Boolean>()

    suspend fun checkUrlStared(url: String){
        isStared.value = repository.checkUrlStared(url)
    }
}