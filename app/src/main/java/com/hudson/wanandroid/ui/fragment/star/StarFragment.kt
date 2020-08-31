package com.hudson.wanandroid.ui.fragment.star

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.fragment.base.ArticlePagerFragment
import kotlinx.coroutines.flow.Flow

class StarFragment : ArticlePagerFragment(), Injectable {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_star, container, false)
    }

    override suspend fun starOrReverseArticle(article: Article) {
        TODO("Not yet implemented")
    }

    override fun getPagingLoadState(): LiveData<PagingRetryLoad>? {
        TODO("Not yet implemented")
    }

    override fun notifyLoadStateChange(retryLoad: PagingRetryLoad) {
        TODO("Not yet implemented")
    }

    override fun loadData(): Flow<PagingData<Article>> {
        TODO("Not yet implemented")
    }

}
