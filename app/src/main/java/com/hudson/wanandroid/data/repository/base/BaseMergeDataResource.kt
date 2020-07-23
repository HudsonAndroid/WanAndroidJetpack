package com.hudson.wanandroid.data.repository.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.MergeData
import com.hudson.wanandroid.data.db.DataWrapperDao
import java.lang.reflect.ParameterizedType

/**
 * 多个请求可以合并的Source
 * Created by Hudson on 2020/7/22.
 */
abstract class BaseMergeDataResource<ResultType, RequestType: MergeData<*,*>>(
    dataWrapperDao: DataWrapperDao,
    appExecutor: AppExecutor
): BaseNetworkBoundResource<ResultType, RequestType>(dataWrapperDao, appExecutor){

    final override fun saveCallResult(item: RequestType) {
        saveFirstData(item.data1)
        saveSecondData(item.data2)
    }

    final override fun loadFromDb(): LiveData<ResultType> {
        val mutableLiveData = MutableLiveData<ResultType>()
        appExecutor.ioExecutor.execute{
            val clazz = getRequestClass()
            val mergeDataType = clazz.genericSuperclass as ParameterizedType
            // MergeData types
            val firstDataType = mergeDataType.actualTypeArguments[0] as Class<*>
            val secondDataType = mergeDataType.actualTypeArguments[1] as Class<*>
            val firstData = loadFirstDataFromDb(firstDataType)
            val secondData = loadSecondDataFromDb(secondDataType)
            val mergeData = clazz.getDeclaredConstructor(firstDataType, secondDataType)
                .newInstance(firstData, secondData)
            mutableLiveData.postValue(transform(mergeData))
        }
        return mutableLiveData
    }

    abstract fun loadFirstDataFromDb(clazz: Class<*>): Any?
    abstract fun loadSecondDataFromDb(clazz: Class<*>): Any?

    abstract fun saveFirstData(data: Any?)
    abstract fun saveSecondData(data: Any?)
}