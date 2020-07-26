package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.Call
import com.hudson.wanandroid.data.common.mergecall.MergeCall
import com.hudson.wanandroid.data.common.mergecall.MergeData
import com.hudson.wanandroid.data.db.DataWrapperDao
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.Banner
import com.hudson.wanandroid.data.entity.HomeArticle
import com.hudson.wanandroid.data.entity.TopArticle
import com.hudson.wanandroid.data.entity.wrapper.ArticleWrapper
import com.hudson.wanandroid.data.entity.wrapper.HomeData
import com.hudson.wanandroid.data.entity.wrapper.HomeDataWrapper
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.data.repository.base.BaseMergeDataResource
import com.hudson.wanandroid.data.repository.base.convertTypeOrNull
import com.hudson.wanandroid.data.repository.base.wrapperCall
import java.lang.reflect.ParameterizedType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 合并三个请求  Banner请求 + TopArticle请求 + HomeArticle请求
 * Created by Hudson on 2020/7/23.
 */
@Singleton
class HomeAllRepository @Inject constructor(
    private val appExecutor: AppExecutor,
    private val wanAndroidApi: WanAndroidApi,
    private val dataWrapperDao: DataWrapperDao
){
    fun loadHomePageData(pageNo: Int): LiveData<Resource<HomeData>>{
        return object : BaseMergeDataResource<HomeData, HomeDataWrapper>(dataWrapperDao, appExecutor){

            override fun shouldFetch(data: HomeData?): Boolean {
                return super.shouldFetch(data) || data == null || data.isEmpty()
            }

            override fun loadFirstDataFromDb(clazz: Class<*>): Any? {
                val mergeDataType = clazz.genericSuperclass as ParameterizedType
                // MergeData types
                val firstType = mergeDataType.actualTypeArguments[0] as Class<*>
                val secondType = mergeDataType.actualTypeArguments[1] as Class<*>
                // 第一个类型是ArticleWrapper，MergeData类型，需要对内部数据进行拆分处理
                //本身有三种数据，任意一种都可能导致204，因此要对数据逐一判断
                if(stashNetworkData?.isSuccessful == true && stashNetworkData?.code() == 204){
                    if(stashNetworkData?.body()?.networkData1Empty == false){ // 说明ArticleWrapper全部有效
                        return stashNetworkData!!.body()!!.data1
                    }
                    // 如果其中某一个网络数据为空，则从数据库获取并与其他一个数据（可能也要从数据库获取）合并
                    val articleWrapper = stashNetworkData?.body()?.data1
                    return articleWrapper?.apply {
                        // 首先判断是否是第一个数据为204或空导致
                        if(networkData1Empty){
                            data1 = convertTypeOrNull(loadDataWrapperFromDb(firstType, identityInfo()))
                        }
                        if(networkData2Empty){
                            data2 = convertTypeOrNull(loadDataWrapperFromDb(secondType, identityInfo()))
                        }
                    }
                }else{
                    // 全部从数据库获取
                    return ArticleWrapper(convertTypeOrNull(loadDataWrapperFromDb(firstType, identityInfo())),
                        convertTypeOrNull(loadDataWrapperFromDb(secondType, identityInfo())))
                }
            }

            override fun loadSecondDataFromDb(clazz: Class<*>): Any? {
                // 第二个类型是Banner，非MergeData类型，直接处理
                return loadDataWrapperFromDb(clazz, identityInfo())
            }

            override fun saveFirstData(data: Any?) {
                val articleWrapper = data as MergeData<*,*>
                if(!articleWrapper.networkData1Empty){
                    saveDataCommon(articleWrapper.data1, identityInfo())
                }
                if(!articleWrapper.networkData2Empty){
                    saveDataCommon(articleWrapper.data2, identityInfo())
                }
            }

            override fun saveSecondData(data: Any?) {
                // 第二个类型是Banner，非MergeData类型，直接处理
                saveDataCommon(data, identityInfo())
            }

            override fun transform(requestType: HomeDataWrapper): HomeData? {
                val articleWrapper = requestType.data1
                val banner = requestType.data2
                // create article list
                val topArticle = articleWrapper?.data1
                val homeArticle = articleWrapper?.data2
                val result = mutableListOf<Article>()
                topArticle?.data?.apply {
                    result.addAll(this)
                }
                homeArticle?.data?.datas?.apply {
                    result.addAll(this)
                }
                return HomeData(banner?.data,result)
            }

            override fun createCall(): Call<HomeDataWrapper> {
                // create banner call
                val bannerCall = if(pageNo == 0){
                    wrapperCall(wanAndroidApi.bannerApi())
                }else{
                    null
                }
                // create top article call
                val topArticleCall = if(pageNo == 0){
                    wrapperCall(wanAndroidApi.topArticle())
                }else{
                    null
                }
                // create home article call
                val homeArticleCall = wrapperCall(wanAndroidApi.homeArticle(pageNo))
                // merge article calls
                val articleCall = object: MergeCall<TopArticle, HomeArticle, ArticleWrapper>(topArticleCall
                    ,homeArticleCall,appExecutor){
                    override fun createTargetMergeDataInstance(): ArticleWrapper {
                        return ArticleWrapper(null,null)
                    }
                }
                // merge all calls
                return object: MergeCall<ArticleWrapper, Banner, HomeDataWrapper>(articleCall,
                    bannerCall,appExecutor){
                    override fun createTargetMergeDataInstance(): HomeDataWrapper {
                        return HomeDataWrapper(null,null)
                    }
                }
            }

            override fun identityInfo(): String {
                return pageNo.toString()
            }

        }.asLiveData()
    }
}