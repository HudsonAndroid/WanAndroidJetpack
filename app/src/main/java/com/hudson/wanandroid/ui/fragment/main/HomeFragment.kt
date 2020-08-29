package com.hudson.wanandroid.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.hudson.wanandroid.data.account.WanAndroidAccount
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.BannerItem
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.wrapper.Resource
import com.hudson.wanandroid.databinding.FragmentHomeBinding
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.adapter.ArticleAdapter
import com.hudson.wanandroid.ui.adapter.BannerAdapter
import com.hudson.wanandroid.ui.adapter.PagingLoadStateAdapter
import com.hudson.wanandroid.ui.adapter.viewholder.ArticleStarClickListener
import com.hudson.wanandroid.ui.common.RetryCallback
import com.hudson.wanandroid.ui.fragment.base.AccountRelativeFragment
import com.hudson.wanandroid.ui.util.autoCleared
import com.hudson.wanandroid.ui.view.indicatorviewpager.listener.SimplePageChangeListener
import com.hudson.wanandroid.viewmodel.HomeModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * 注入代码在AppInjector中完成
 *
 * 目前来看，谷歌的Navigation导航组件
 * 中fragment是通过replace方式实现的，
 * 因此按下返回实际上是创建了新的fragment，
 * 导致旧fragment中的信息没有被暂存，
 * 因此我们需要手动存储包括数据、视图状态等
 * 的信息，这将是一个棘手的问题，目前下面
 * 代码没有对视图信息做暂存操作。相关问题
 * 可以追踪：
 * https://github.com/android/architecture-components-samples/issues/530
 *
 * 这是一个待处理的问题
 *
 * 2020-8-7 自定义Navigator，使用hide/show替换系统replace方案
 *
 * 2020-8-10 实践发现，在Navigation结合BottomNavigationView时，使用
 * 默认的Navigator是会重新创建Fragment，而在SearchActivity导航中，
 * 默认Fragment不会重新创建（init不会调用），但会重新调用onCreateView
 * 和onViewCreated
 * Created by Hudson on 2020/7/12.
 */
class HomeFragment: AccountRelativeFragment() , Injectable{
    private var homeBinding by autoCleared<FragmentHomeBinding>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // 以下来源自fragment-ktx kotlin扩展库，用于创建ViewModel，
    // 相当于ViewModelProvider(fragment, factory).get(BannerModel::class.java)
    // 2020-8-6 由viewModels修改为activityViewModels，原因是：
    // fragment作为activity内部的一个tab页面，重新切换到该页面时不应该重新获取数据
    // 因此需要提高ViewModel的Lifecycle级别至activity级别

    // 2020-8-7 由于自定义了Navigator，修改了系统Navigation的replace方案，因此改回
    // viewModels
    val homeModel: HomeModel by viewModels {
        viewModelFactory
    }

    private val starClickListener = object: ArticleStarClickListener{
        override fun onStarClick(article: Article) {
            lifecycleScope.launch {
                homeModel.starArticle(article)
            }
        }
    }

    private val articleAdapter: ArticleAdapter by lazy {
        ArticleAdapter(starClickListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //注释的内容将通过Dagger注入得到ViewModelProvider.Factory，然后根据它创建BannerModel
        //因此外界并不知道BannerModel如何实现的，BannerModel如果要修改，不会影响到HomeFragment
//        //应该使用ViewModelProvider来构建
//        val bannerModel = BannerModel(
//            HomeRepository(
//                WanAndroidApi.api(),
//                WanAndroidDb.getInstance(context!!).dataWrapperDao()
//            )
//        )
        homeBinding.adapter = BannerAdapter()
        homeBinding.bannerCount = homeModel.bannerCount
        homeBinding.bannerTitle = homeModel.bannerTitle
        homeBinding.indicator = homeBinding.ppIndicator // can we simplify?
        homeBinding.pageChangeListener = object : SimplePageChangeListener(){
            override fun onPageSelected(position: Int) {
                homeModel.onPageChanged(position)
            }
        }
        homeBinding.retry = object : RetryCallback {
            override fun retry() {
                homeModel.retry()
                articleAdapter.retry()
            }
        }
        homeBinding.banners = homeModel.bannersLiveData
        homeBinding.pagingRetry = homeModel.articleLoadState
        homeBinding.lifecycleOwner = this //自动为LiveData设置数据观察绑定，相当于liveData.observe自动完成

        homeModel.bannersLiveData.observe(viewLifecycleOwner,
            Observer<Resource<List<BannerItem>>> {
                homeBinding.adapter!!.refresh(homeBinding.vpBanner,it.data)
                homeModel.update()
                homeBinding.vpBanner.startAutoSwitch()
                Timber.d("${it.status}, ${it.data}, ${it.msg}")
            })
    }

    override fun onAccountInitialed(user: LoginUser) {
        attachArticles()
    }

    override fun onAccountChanged(user: LoginUser) {
        // 重新加载
        articleAdapter.refresh()
    }

    private fun attachArticles(){
        val retryLoad = PagingRetryLoad()
        articleAdapter.addLoadStateListener { loadState ->
            retryLoad.loadStates = loadState.mediator
            retryLoad.hasShowData = articleAdapter.itemCount != 0
            homeModel.articleLoadState.value = retryLoad
        }
        homeBinding.rvList.adapter = articleAdapter.withLoadStateFooter(PagingLoadStateAdapter(articleAdapter))

        lifecycleScope.launch {
            @OptIn(ExperimentalCoroutinesApi::class)
            homeModel.articles.collectLatest { // collectLatest主要是为了背压处理
                articleAdapter.submitData(it)
            }
        }
    }
}