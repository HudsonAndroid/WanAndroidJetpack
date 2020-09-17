package com.hudson.wanandroid.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.hudson.wanandroid.R
import com.hudson.wanandroid.databinding.ActivityArticleBinding
import com.hudson.wanandroid.ui.fragment.other.AskArticleFragment
import com.hudson.wanandroid.ui.fragment.other.SquareArticleFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

class ArticleActivity : AppCompatActivity(), HasSupportFragmentInjector {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val articleType = intent.getIntExtra(KEY_TYPE, TYPE_ASK)
        val binding =
            DataBindingUtil.setContentView<ActivityArticleBinding>(this, R.layout.activity_article)
        if(savedInstanceState == null){
            binding.tvTitle.setText(getTitle(articleType))
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fl_fragment_container, getFragment(articleType)).commit()
        }
    }

    private fun getFragment(type: Int): Fragment{
        return when(type){
            TYPE_ASK -> AskArticleFragment()
            TYPE_SQUARE -> SquareArticleFragment()
            else -> AskArticleFragment()
        }
    }

    private fun getTitle(type: Int): Int{
        return when(type){
            TYPE_ASK -> R.string.menu_ask
            TYPE_SQUARE -> R.string.menu_square
            else -> R.string.menu_ask
        }
    }

    companion object{
        const val TYPE_ASK = 0
        const val TYPE_SQUARE = 1
        private const val KEY_TYPE = "article_type"

        fun start(context: Context, type: Int){
            if(type == TYPE_ASK || type == TYPE_SQUARE){
                val intent = Intent(context, ArticleActivity::class.java)
                intent.putExtra(KEY_TYPE, type)
                context.startActivity(intent)
            }else{
                Timber.e("The article type you give is invalid")
            }
        }
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector
}