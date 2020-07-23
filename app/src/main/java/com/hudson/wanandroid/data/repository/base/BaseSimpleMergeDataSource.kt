package com.hudson.wanandroid.data.repository.base

import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.MergeData
import com.hudson.wanandroid.data.db.DataWrapperDao

/**
 * 针对MergeData的两个类型都是Json数据，可以被DataWrapper包装的Source封装
 * Created by Hudson on 2020/7/22.
 */
abstract class BaseSimpleMergeDataSource<ResultType, RequestType: MergeData<*, *>>(
    dataWrapperDao: DataWrapperDao,
    appExecutor: AppExecutor
): BaseMergeDataResource<ResultType, RequestType>(dataWrapperDao, appExecutor){

    override fun loadFirstDataFromDb(clazz: Class<*>): Any? {
        return loadDataWrapperFromDb(clazz, firstIdentityInfo())
    }

    override fun loadSecondDataFromDb(clazz: Class<*>): Any? {
        return loadDataWrapperFromDb(clazz, secondIdentityInfo())
    }

    override fun saveFirstData(data: Any?) {
        saveDataCommon(data, firstIdentityInfo())
    }

    override fun saveSecondData(data: Any?) {
        saveDataCommon(data, secondIdentityInfo())
    }

    open fun firstIdentityInfo() = ""

    open fun secondIdentityInfo() = ""
}