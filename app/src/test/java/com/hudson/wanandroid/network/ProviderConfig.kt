package com.hudson.wanandroid.network

import com.hudson.wanandroid.network.impl.banner.SuccessBannerProvider
import com.hudson.wanandroid.network.impl.homearticle.SuccessHomeArticleProvider
import com.hudson.wanandroid.network.impl.toparticle.SuccessTopArticleProvider
import com.hudson.wanandroid.network.interfaces.IBannerProvider
import com.hudson.wanandroid.network.interfaces.IHomeArticleProvider
import com.hudson.wanandroid.network.interfaces.ITopArticleProvider

/**
 * Created by Hudson on 2020/7/24.
 */
class ProviderConfig private constructor(val bannerProvider: IBannerProvider,
                     val homeArticleProvider: IHomeArticleProvider,
                     val topArticleProvider: ITopArticleProvider) {

    class Builder private constructor(private var bannerProvider: IBannerProvider,
                  private var homeArticleProvider: IHomeArticleProvider,
                  private var topArticleProvider: ITopArticleProvider){

        constructor(): this(SuccessBannerProvider(),
                        SuccessHomeArticleProvider(),
                        SuccessTopArticleProvider())

        fun bannerProvider(bannerProvider: IBannerProvider): Builder{
            this.bannerProvider = bannerProvider
            return this
        }

        fun homeArticleProvider(homeArticleProvider: IHomeArticleProvider): Builder{
            this.homeArticleProvider = homeArticleProvider
            return this
        }

        fun topArticleProvider(topArticleProvider: ITopArticleProvider): Builder{
            this.topArticleProvider = topArticleProvider
            return this
        }

        fun build(): ProviderConfig{
            return ProviderConfig(bannerProvider,homeArticleProvider,topArticleProvider)
        }
    }
}