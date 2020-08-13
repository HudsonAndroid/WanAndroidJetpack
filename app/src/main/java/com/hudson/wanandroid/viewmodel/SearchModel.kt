package com.hudson.wanandroid.viewmodel

import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagingData
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.HotWord
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.repository.HotSearchWordRepository
import com.hudson.wanandroid.viewmodel.util.AbsentLiveData
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 共享型ViewModel
 * Created by Hudson on 2020/8/10.
 */
class SearchModel @Inject constructor(private val repository: HotSearchWordRepository): ViewModel(){

    val searchWord = MutableLiveData<String>()

    // 不会立刻执行相关加载逻辑，除非有监听者
    val hotSearchWord = repository.loadHotSearchWord()

    // 历史搜索
    val historySearch = repository.loadHistorySearch()

    // 结果页面加载状态
    val searchLoadState = MutableLiveData<PagingRetryLoad>()

    var searchResult:LiveData<PagingData<Article>> = searchWord.switchMap {
        if(it.isBlank()){
            AbsentLiveData.create()
        }else{
            viewModelScope.launch {
                repository.storeHistorySearch(HotWord(name = it))
            }
            repository.fetchSearchResult(it).asLiveData()
        }
    }

    val onCleanClick = View.OnClickListener {
        viewModelScope.launch {
            repository.cleanHistorySearch()
        }
    }

}