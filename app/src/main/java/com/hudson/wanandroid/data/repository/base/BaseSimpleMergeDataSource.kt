package com.hudson.wanandroid.data.repository.base

import com.google.gson.Gson
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

    private fun loadDataWrapperFromDb(clazz: Class<*>, identityInfo: String): Any?{
        val wrapper = loadDataWrapper(
            clazz,
            dataWrapperDao,
            identityInfo
        )
        //if one of MergeData is expired, the data is expired
        checkDataExpire(wrapper)
        return wrapper?.content?.run {
            Gson().fromJson(this, clazz)
        }
    }

    private fun saveDataCommon(data: Any?, identityInfo: String){
        data?.apply {
            saveDataWrapper(
                data.javaClass,
                data,
                dataWrapperDao,
                identityInfo
            )
        }
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