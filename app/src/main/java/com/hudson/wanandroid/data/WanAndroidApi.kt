package com.hudson.wanandroid.data

import com.hudson.wanandroid.data.entity.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Created by Hudson on 2020/7/11.
 */
interface WanAndroidApi {
    private object Inner{
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        val singleTon: WanAndroidApi = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WanAndroidApi::class.java)
    }

    companion object{
        private const val BASE_URL = "https://www.wanandroid.com"

        fun api():WanAndroidApi{
            return Inner.singleTon
        }
    }

    @GET("banner/json")
    fun bannerApi(): Call<Banner>

    @GET("article/top/json")
    fun topArticle(): Call<TopArticle>

    @GET("article/list/{pageNo}/json")
    fun homeArticle(@Path("pageNo") pageNo: Int): Call<ArticleResultWrapper>

    @GET("friend/json")
    fun websiteApi(): Call<Website>

    @GET("hotkey/json")
    fun searchHotWordApi(): Call<SearchHotWord>

    @GET("project/tree/json")
    fun projectsCategory(): Call<Projects>

    @GET("tree/json")
    fun treeCategory(): Call<SubjectWrapper>

    @GET("wxarticle/chapters/json")
    fun wechatCategory(): Call<SubjectWrapper>

    // 注意记得添加suspend标志符，否则会报错：java.lang.IllegalArgumentException: Unable to create call adapter for class
    // 这个错误表面上是CallAdapter不对应，实际是没有添加suspend处理导致
    @GET("wxarticle/list/{wechatId}/{pageNo}/json")
    suspend fun wechatItemList(@Path("wechatId")wechatId: Int,
        @Path("pageNo")pageNo: Int): ArticleResultWrapper

    @GET("navi/json")
    fun navigation(): Call<NavigationWrapper>

    @GET("article/list/{pageNo}/json")
    suspend fun treeItemList(@Path("pageNo")pageNo: Int,
                             @Query("cid")treeId: Int): ArticleResultWrapper

    // Method注解中是路径，不要携带参数
    // @Path 和 @Query是不同的针对目标，不可混用
    @GET("project/list/{pageNo}/json")
    suspend fun projectItemList(@Path("pageNo")pageNo: Int,
                        @Query("cid")projectId: Int): ArticleResultWrapper

    @POST("article/query/{pageNo}/json")
    @FormUrlEncoded
    suspend fun searchHotResult(@Path("pageNo") pageNo: Int,
                                @Field("k") searchWord: String): ArticleResultWrapper
}