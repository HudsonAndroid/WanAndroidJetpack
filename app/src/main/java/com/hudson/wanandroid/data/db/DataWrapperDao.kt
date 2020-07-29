package com.hudson.wanandroid.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hudson.wanandroid.data.entity.wrapper.DataWrapper

/**
 * Created by Hudson on 2020/7/14 0014.
 */
@Dao
interface DataWrapperDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: DataWrapper)

    //表名不区分大小写
    @Query("SELECT * FROM datawrapper WHERE dataType = :dataType AND customInfo = :customInfo")
    fun queryExactly(dataType: String, customInfo: String): DataWrapper?

    @Query("SELECT * FROM datawrapper WHERE dataType = :dataType")
    fun query(dataType: String): DataWrapper?
}