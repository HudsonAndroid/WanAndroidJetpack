package com.hudson.wanandroid.viewmodel.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Created by Hudson on 2020/8/31.
 */
@BindingAdapter(value = ["bindLoadState","loadingStr","normalStr"], requireAll = false)
fun bindLoadState(textView: TextView, loading: Boolean?, loadingStr: String, normalStr: String){
    if(loading == true){
        textView.text = loadingStr
    }else{
        textView.text = normalStr
    }
}