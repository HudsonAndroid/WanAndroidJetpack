package com.hudson.wanandroid.data.entity

/**
 * 搜索热词
 * Created by Hudson on 2020/7/12.
 */
data class SearchHotWord(
    val data:List<HotWord>
):BaseResult(){
    override fun toString(): String {
        return "SearchHotWord(data=$data)"
    }
}

data class HotWord(
    val id:Int,
    val link:String,
    val name:String,
    val order:Int,
    val visible:Int
){
    override fun toString(): String {
        return "HotWord(id=$id, link='$link', name='$name', order=$order, visible=$visible)"
    }
}
