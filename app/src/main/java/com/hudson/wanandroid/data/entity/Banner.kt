package com.hudson.wanandroid.data.entity

/**
 * banner数据
 * Created by Hudson on 2020/7/11.
 */
data class Banner(
    val data:List<BannerItem>
): BaseResult(){

    override fun toString(): String {
        return super.toString() + "Banner(data=$data)"
    }
}


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