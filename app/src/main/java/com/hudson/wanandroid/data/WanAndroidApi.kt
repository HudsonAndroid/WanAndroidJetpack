package com.hudson.wanandroid.data

import com.hudson.wanandroid.data.entity.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Hudson on 2020/7/11.
 */
interface WanAndroidApi {
    private object Inner{
        val singleTon: WanAndroidApi = Retrofit.Builder()
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
    fun homeArticle(@Path("pageNo") pageNo: Int): Call<HomeArticle>

    @GET("friend/json")
    fun websiteApi(): Call<Website>

    @GET("hotkey/json")
    fun searchHotWordApi(): Call<SearchHotWord>
}