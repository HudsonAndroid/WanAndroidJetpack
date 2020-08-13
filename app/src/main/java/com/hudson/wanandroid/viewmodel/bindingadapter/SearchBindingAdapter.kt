package com.hudson.wanandroid.viewmodel.bindingadapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.BindingAdapter
import com.hudson.wanandroid.data.entity.HotWord
import com.hudson.wanandroid.databinding.ItemSearchWordBinding
import com.hudson.wanandroid.ui.view.AutoAdaptLayout

// 最多显示的个数
const val MAX_SHOW_WORD_COUNT = 20

/**
 * Created by Hudson on 2020/8/10.
 */
@BindingAdapter(value = ["hotWords", "wordClickListener","reversed"], requireAll = false)
fun hotWords(container: AutoAdaptLayout, hotWords: List<HotWord>?,
             wordClickListener: View.OnClickListener?,
            reversed: Boolean?){
    if(hotWords != null && hotWords.isNotEmpty()){
        container.removeAllViews()
        var target: List<HotWord> = hotWords
        if(reversed == true){
            target = manageWordCount(hotWords)
            target =target.reversed()
        }
        target.forEach {
            val binding =
                ItemSearchWordBinding.inflate(LayoutInflater.from(container.context), container, true)
            binding.clickListener = wordClickListener
            binding.word = it
        }
    }
}

private fun manageWordCount(hotWords: List<HotWord>): List<HotWord>{
    if(hotWords.size > MAX_SHOW_WORD_COUNT){
        return hotWords.subList(hotWords.size - MAX_SHOW_WORD_COUNT, hotWords.size)
    }
    return hotWords
}

@BindingAdapter(value = ["visibleOrGone"])
fun visibleOrGone(view: View, hotWords: List<HotWord>?){
    if(hotWords != null && hotWords.isNotEmpty()){
        view.visibility = VISIBLE
    }else {
        view.visibility = GONE
    }
}

