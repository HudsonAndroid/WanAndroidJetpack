package com.hudson.wanandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.HomeRepository

/**
 * Created by Hudson on 2020/7/11.
 */
class BannerModel(private val repository: HomeRepository): ViewModel() {
    val bannerTitle = MutableLiveData<String>()
    val bannerCount = MutableLiveData<Int>()
    val bannersLiveData: MediatorLiveData<Resource<List<BannerItem>>> = MediatorLiveData()
    var oldData: LiveData<Resource<List<BannerItem>>>? = null

    init {
        bannersLiveData.addSource(repository.loadBanners()){
            bannersLiveData.value = it
        }
    }

    fun onPageChanged(position: Int){
        bannerTitle.value = bannersLiveData.value?.data?.get(position)?.title
    }

    fun retry(){
        bannersLiveData.addSource(repository.loadBanners()){
            oldData?.apply {
                bannersLiveData.removeSource(oldData!!)
            }
            bannersLiveData.value = it
        }
    }

    //data load in, invoke
    fun refresh(){
        bannersLiveData.value?.data?.apply {
            if(isNotEmpty()){
                bannerTitle.value = this[0].title //title for first time
            }
            bannerCount.value = this.size
        }
    }

    override fun onCleared() {
        super.onCleared()
        oldData?.apply {
            bannersLiveData.removeSource(oldData!!)
        }
    }

}