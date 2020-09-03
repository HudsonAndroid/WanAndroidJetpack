package com.hudson.wanandroid.ui.fragment.wechat

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.hudson.wanandroid.data.entity.Article
import com.hudson.wanandroid.data.entity.PagingRetryLoad
import com.hudson.wanandroid.data.entity.Subject
import com.hudson.wanandroid.di.Injectable
import com.hudson.wanandroid.ui.fragment.base.ArticlePagerFragment
import com.hudson.wanandroid.viewmodel.WechatItemModel
import javax.inject.Inject


private const val ARG_SUBJECT = "subject"

/**
 * Created by Hudson on 2020/8/26.
 */
class WechatItemFragment: ArticlePagerFragment(), Injectable{
    lateinit var subject: Subject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subject = it.getSerializable(ARG_SUBJECT) as Subject
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val wechatItemModel: WechatItemModel by viewModels {
        viewModelFactory
    }

    override suspend fun starOrReverseArticle(article: Article)
            = wechatItemModel.starOrReverseArticle(requireActivity(), article)

    override fun getPagingLoadState() = wechatItemModel.loadState

    override fun notifyLoadStateChange(retryLoad: PagingRetryLoad) {
        wechatItemModel.loadState.value = retryLoad
    }

    override fun loadData() = wechatItemModel.loadData(subject.id, subject.parentChapterId)

    companion object{

        // keep fragment constructor without param
        @JvmStatic
        fun newInstance(subject: Subject) =
            WechatItemFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_SUBJECT, subject)
                }
            }
    }
}