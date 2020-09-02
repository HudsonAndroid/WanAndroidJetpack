package com.hudson.wanandroid.viewmodel.bindingadapter

import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hudson.wanandroid.R
import com.hudson.wanandroid.data.entity.LoginUser
import com.hudson.wanandroid.ui.activity.AccountManageActivity
import com.hudson.wanandroid.ui.activity.LoginActivity
import jp.wasabeef.glide.transformations.CropCircleTransformation

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

@BindingAdapter(value = ["nickName","maxWords"])
fun bindNickName(textView: TextView, nickName: String?, count: Int?){
    if(nickName.isNullOrEmpty()){
        textView.setText(R.string.tips_not_login)
        textView.setOnClickListener {
            LoginActivity.start(textView.context)
        }
    }else{
        textView.text = nickName
        textView.setOnClickListener(null)
    }

    // 由于系统针对maxLength的省略号不生效，因此手动处理
    count?.run {
        val text = textView.text.toString()
        if(text.length > count){
            textView.text = "${text.substring(0, count)}..."
        }
    }
}

@BindingAdapter(value = ["avatar", "loginUser"], requireAll = true)
fun userAvatar(imageView: ImageView, iconUrl: String?, loginUser: LoginUser?){
    // todo 找一张圆形的默认图片，因为glide不会对默认的占位符或者错误图片处理
    // 见https://github.com/bumptech/glide/issues/3335
    Glide.with(imageView.context)
        .load(iconUrl)
        .placeholder(R.drawable.icon_default_user)
        .error(R.drawable.icon_default_user)
        .bitmapTransform(CropCircleTransformation(imageView.context))
        .into(imageView)

    imageView.setOnClickListener {
        if(loginUser == null){
            LoginActivity.start(imageView.context)
        }else{
            AccountManageActivity.start(imageView.context)
        }
    }
}