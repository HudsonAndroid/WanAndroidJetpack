package com.hudson.wanandroid.data.entity

import androidx.room.Entity

/**
 * Created by Hudson on 2020/7/21.
 */
@Entity(primaryKeys = ["id", "chapterId"])
data class Article(
//    @PrimaryKey (不要直接使用id作为主键，因为ROOM会按照id大小顺序存储，导致最后文章先后顺序错乱)
    val id: Int,//如果位数过长，可以考虑Long
    val apkLink: String,
    val audit: Int,
    val author: String,
    val canEdit: Boolean,
    val chapterId: Int,
    val chapterName: String,
    var collect: Boolean,
    val courseId: Int,
    val desc: String,
    val descMd: String,
    val envelopePic: String,
    val fresh: Boolean,
    val link: String,
    val niceDate: String,
    val niceShareDate: String,
    val origin: String,
    val prefix: String,
    val projectLink: String,
    val publishTime: Long,
    val realSuperChapterId: Int,
    val selfVisible: Int,
    val shareDate: Long,
    val shareUser: String,
    val superChapterId: Int,
    val superChapterName: String,
    val tags: List<Tag>,
    val title: String,
    val type: Int,
    val userId: Int,
    val visible: Int,
    val zan: Int
){
//    override fun toString(): String {
//        return "Article(apkLink='$apkLink', audit=$audit, author='$author', canEdit=$canEdit, chapterId=$chapterId, chapterName='$chapterName', collect=$collect, courseId=$courseId, desc='$desc', descMd='$descMd', envelopePic='$envelopePic', fresh=$fresh, id=$id, link='$link', niceDate='$niceDate', niceShareDate='$niceShareDate', origin='$origin', prefix='$prefix', projectLink='$projectLink', publishTime=$publishTime, realSuperChapterId=$realSuperChapterId, selfVisible=$selfVisible, shareDate=$shareDate, shareUser='$shareUser', superChapterId=$superChapterId, superChapterName='$superChapterName', tags=$tags, title='$title', type=$type, userId=$userId, visible=$visible, zan=$zan)"
//    }

    override fun toString(): String {
        return "Article(title=$title)"
    }
}

// 由于收藏文章列表接口返回的article中部分属性比其他接口返回的Article少，且没有collect属性多originId等属性，因此
// 单独创建实例
@Entity(primaryKeys = ["id", "chapterId"])
data class StarArticle(
//    @PrimaryKey (不要直接使用id作为主键，因为ROOM会按照id大小顺序存储，导致最后文章先后顺序错乱)
    val id: Int,//如果位数过长，可以考虑Long
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val link: String,
    val niceDate: String,
    val origin: String,
    val originId: Int?,
    val publishTime: Long,
    val title: String,
    val userId: Int,
    val visible: Int,
    val zan: Int
)