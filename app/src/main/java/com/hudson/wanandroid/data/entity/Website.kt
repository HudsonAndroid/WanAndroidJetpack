package com.hudson.wanandroid.data.entity

/**
 * 常用网站
 * Created by Hudson on 2020/7/12.
 */
data class Website(
    val data: List<WebsiteItem>
):BaseResult(){
    override fun toString(): String {
        return "Website(data=$data)"
    }
}

data class WebsiteItem(
    val icon:String,
    val id:Int,
    val link:String,
    val name:String,
    val order:Int,
    val visible:Int
) {

    override fun toString(): String {
        return "Website(icon='$icon', id=$id, link='$link', name='$name', order=$order, visible=$visible)"
    }
}