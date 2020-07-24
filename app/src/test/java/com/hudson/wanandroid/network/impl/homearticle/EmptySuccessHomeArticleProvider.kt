package com.hudson.wanandroid.network.impl.homearticle

import com.hudson.wanandroid.network.common.provideEmptySuccessData
import com.hudson.wanandroid.network.interfaces.IHomeArticleProvider
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Hudson on 2020/7/24.
 */
class EmptySuccessHomeArticleProvider : IHomeArticleProvider{
    override fun provideHomeArticle(chain: Interceptor.Chain, pageNo: Int): Response {
        return provideEmptySuccessData(chain)
    }

}
