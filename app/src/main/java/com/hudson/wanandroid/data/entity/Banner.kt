package com.hudson.wanandroid.data.entity

/**
 * Created by Hudson on 2020/7/11.
 */
data class Banner(
    val data:List<BannerItem>
): BaseResult(){

    override fun toString(): String {
        return super.toString() + "Banner(data=$data)"
    }
}


/**
 * "desc": "享学~",
"id": 29,
"imagePath": "https://wanandroid.com/blogimgs/2087429c-f6ac-43dd-9ffe-8af871b6deb8.png",
"isVisible": 1,
"order": 0,
"title": "&ldquo;学不动了，也得学！&rdquo;",
"type": 0,
"url": "https://mp.weixin.qq.com/s/PRv6SAZlklz4DRm1EsBdew"
 */
data class BannerItem(
    val desc: String,
    val id:Int, //如果位数长，考虑Long
    val imagePath:String,
    val isVisible: Int,
    val order:Int,
    val title:String,
    val type:Int,
    val url:String
){
    override fun toString(): String {
        return "BannerItem(desc='$desc', id=$id, imagePath='$imagePath', isVisible=$isVisible, order=$order, title='$title', type=$type, url='$url')"
    }
}