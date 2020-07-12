package com.hudson.wanandroid.data

import com.hudson.wanandroid.data.entity.Banner
import com.hudson.wanandroid.data.entity.SearchHotWord
import com.hudson.wanandroid.data.entity.Website
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Hudson on 2020/7/11.
 */
interface WanAndroidApi {
    companion object{
        private const val BASE_URL = "https://www.wanandroid.com"

        fun api():WanAndroidApi{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(WanAndroidApi::class.java)
        }
    }

    @GET("banner/json")
    fun bannerApi(): Call<Banner>

    @GET("friend/json")
    fun websiteApi(): Call<Website>

    @GET("hotkey/json")
    fun searchHotWordApi(): Call<SearchHotWord>
}