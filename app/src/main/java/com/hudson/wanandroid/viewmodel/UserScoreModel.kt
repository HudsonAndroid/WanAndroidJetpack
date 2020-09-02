package com.hudson.wanandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.repository.UserScoreRepository
import javax.inject.Inject

/**
 * Created by Hudson on 2020/9/1.
 */
class UserScoreModel @Inject constructor(private val repository: UserScoreRepository): ViewModel(){

    fun loadCurrentUserScore(userId: Long) = repository.loadCurrentUserScore(userId)
}