package com.hudson.wanandroid.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hudson.wanandroid.data.entity.wrapper.BaseResult

/**
 * 搜索热词
 * Created by Hudson on 2020/7/12.
 */
data class SearchHotWord(
    val data:List<HotWord>
): BaseResult(){
    override fun toString(): String {
        return "SearchHotWord(data=$data)"
    }
}

// 内部只有name有实际用途，其他貌似没用，为了匹配上服务端数据，因此历史搜索也使用该数据实体
@Entity
data class HotWord(
    val id:Int = -1,
    val link:String = "",
    @PrimaryKey
    val name:String,
    val order:Int = -1,
    val visible:Int = -1
){
    override fun toString(): String {
        return "HotWord(id=$id, link='$link', name='$name', order=$order, visible=$visible)"
    }
}
