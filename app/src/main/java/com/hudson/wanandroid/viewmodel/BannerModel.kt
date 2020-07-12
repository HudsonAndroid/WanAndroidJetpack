package com.hudson.wanandroid.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.entity.Banner
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.ui.adapter.BannerAdapter
import com.hudson.wanandroid.ui.view.indicatorviewpager.indicator.IPagerIndicator
import com.hudson.wanandroid.ui.view.indicatorviewpager.listener.SimplePageChangeListener
import com.hudson.wanandroid.ui.view.indicatorviewpager.viewpager.AutoSwitchViewPager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Hudson on 2020/7/11.
 */
class BannerModel: ViewModel() {
    var banners:List<BannerItem>? = null
    val bannerTitle = MutableLiveData<String>()
    var adapter = BannerAdapter()
    private var viewPager: AutoSwitchViewPager? = null
    var indicator: IPagerIndicator? = null
    val bannerCount = MutableLiveData<Int>()

    init {
//        GlobalScope.launch(Dispatchers.Main) {
//            withContext(Dispatchers.IO){
//
//
//            }
//        }
        WanAndroidApi.api().bannerApi().enqueue(object : Callback<Banner>{
            override fun onFailure(call: Call<Banner>?, t: Throwable?) {
                t?.printStackTrace()
            }

            override fun onResponse(call: Call<Banner>?, response: Response<Banner>?) {
                Log.e("hudson","数据时${response?.body()}")
                banners = response?.body()?.data
                bindBanner()
            }
        })
    }

    private fun bindBanner(){
        if(banners != null && viewPager != null){
            adapter.refresh(viewPager!!,banners)
            if(banners!!.isNotEmpty()){
                //刷新首次的标题
                bannerTitle.value = banners?.get(0)?.title
            }
            //更新indicator圆点个数
            bannerCount.value = banners?.size ?: 0
        }
    }

    fun bindViewPager(viewPager:AutoSwitchViewPager){
        this.viewPager = viewPager
        bindBanner()
    }

    val bannerChangeListener = object : SimplePageChangeListener(){
        override fun onPageSelected(position: Int) {
            bannerTitle.value = banners?.get(position)?.title
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewPager?.clearOnPageChangeListeners()
    }

}