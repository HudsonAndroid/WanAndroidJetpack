package com.hudson.wanandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.repository.NavigationRepository
import javax.inject.Inject

/**
 * Created by Hudson on 2020/8/25.
 */
class NavigationModel @Inject constructor(private val repository: NavigationRepository): ViewModel() {

    val navigationList = repository.loadNavigation()

    fun retry(){
        repository.retry()
    }
}