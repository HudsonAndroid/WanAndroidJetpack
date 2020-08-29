package com.hudson.wanandroid.data.network

import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.account.WanAndroidAccount
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Cookie manager
 * Created by Hudson on 2020/8/27.
 */
class WanAndroidCookieJar : CookieJar{

    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        if(url.toString().endsWith(WanAndroidApi.LOGIN_PATH)){
            WanAndroidAccount.cookieCache = cookies
        }
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        return WanAndroidAccount.cookieCache ?: mutableListOf()
    }

}