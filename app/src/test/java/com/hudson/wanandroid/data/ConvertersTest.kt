package com.hudson.wanandroid.data

import com.hudson.wanandroid.data.db.DbMemberConverters
import com.hudson.wanandroid.data.entity.Tag
import com.hudson.wanandroid.network.common.readFile
import org.junit.Assert.*
import org.junit.Test

/**
 * Room数据转换测试
 * Created by Hudson on 2020/8/6.
 */
class ConvertersTest {

    @Test
    fun testDbMemberConverters(){
        val tags = mutableListOf<Tag>()
        tags.add(Tag("baidu","https://www.baidu.com"))
        tags.add(Tag("google", "https://www.google.com"))

        // entity array to json test
        val fileJson = readFile("article-tag.json")
        assertEquals(
            fileJson,
            DbMemberConverters().articleTag2Json(tags))

        // json to entity array test
        val transformTags = DbMemberConverters().json2ArticleTag(fileJson)
        assertNotNull(transformTags)
        transformTags?.run {
            assertEquals(tags[0], transformTags[0])
            assertEquals(tags[1], transformTags[1])
        }
    }

}