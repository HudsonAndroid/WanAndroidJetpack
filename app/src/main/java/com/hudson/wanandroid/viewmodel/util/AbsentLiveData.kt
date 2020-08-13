package com.hudson.wanandroid.viewmodel.util

import androidx.lifecycle.LiveData

/**
 * Created by Hudson on 2020/8/12.
 */
class AbsentLiveData <T: Any?> private constructor(): LiveData<T>(){
    init {
        postValue(null)
    }

    companion object{
        fun <T> create(): LiveData<T> {
            return AbsentLiveData()
        }
    }
}