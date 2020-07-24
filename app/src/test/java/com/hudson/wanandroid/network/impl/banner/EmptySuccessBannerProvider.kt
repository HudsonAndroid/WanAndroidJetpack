package com.hudson.wanandroid.network.impl.banner

import com.hudson.wanandroid.network.common.provideEmptySuccessData
import com.hudson.wanandroid.network.interfaces.IBannerProvider
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by Hudson on 2020/7/24.
 */
class EmptySuccessBannerProvider : IBannerProvider{

    override fun provideBanner(chain: Interceptor.Chain): Response {
        return provideEmptySuccessData(chain)
    }

}