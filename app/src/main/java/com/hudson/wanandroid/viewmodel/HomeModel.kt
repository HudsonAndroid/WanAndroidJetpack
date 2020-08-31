package com.hudson.wanandroid.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.repository.HomeRepository
import javax.inject.Inject

/**
 * 创建方式由Dagger注入
 *
 * API数据由BannerModel缓存，Fragment
 * 本身生命周期不受约束（即按照Navigation的replace fragment方式）
 * Created by Hudson on 2020/7/11.
 */
class HomeModel @Inject constructor(private val repository: HomeRepository): ViewModel() {
    val bannerTitle = MutableLiveData<String>()
    val bannerCount = MutableLiveData<Int>()
    val bannersLiveData = repository.loadBanners()
    val articleLoadState = MutableLiveData<PagingRetryLoad>()
    // cachedIn限定其范围，避免泄漏
    // 不依赖fragment触发加载，因为本身viewModel生命周期长于fragment(生命周期更长的主要原因是tab切换避免重复加载)
    val articles = repository.loadArticles().cachedIn(viewModelScope)

    fun onPageChanged(position: Int){
        bannerTitle.value = bannersLiveData.value?.data?.get(position)?.title
    }

    fun retry(){
        repository.retry()
    }

    suspend fun starArticle(context: Context, article: Article) = repository.starArticle(context, article)

    //data load in, invoke
    fun update(){
        bannersLiveData.value?.data?.apply {
            if(isNotEmpty()){
                bannerTitle.value = this[0].title //title for first time
            }
            bannerCount.value = this.size
        }
    }

}