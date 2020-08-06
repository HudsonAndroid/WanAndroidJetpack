package com.hudson.wanandroid.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.HomeRepository
import javax.inject.Inject

/**
 * 创建方式由Dagger注入
 * Created by Hudson on 2020/7/11.
 */
class BannerModel @Inject constructor(private val repository: HomeRepository): ViewModel() {
    val bannerTitle = MutableLiveData<String>()
    val bannerCount = MutableLiveData<Int>()
    val bannersLiveData: MediatorLiveData<Resource<List<BannerItem>>> = MediatorLiveData()
    var oldData: LiveData<Resource<List<BannerItem>>>? = null
    val articleLoadState = MutableLiveData<PagingRetryLoad>()

    init {
        bannersLiveData.addSource(repository.loadBanners()){
            bannersLiveData.value = it
        }
    }

    // cachedIn限定其范围，避免泄漏
    fun getArticles() = repository.loadArticles().cachedIn(viewModelScope)

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