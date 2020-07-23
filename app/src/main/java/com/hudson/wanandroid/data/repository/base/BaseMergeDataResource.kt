package com.hudson.wanandroid.data.repository.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.MergeData
import com.hudson.wanandroid.data.db.DataWrapperDao
import retrofit2.Response
import java.lang.reflect.ParameterizedType

/**
 * 多个请求可以合并的Source
 * Created by Hudson on 2020/7/22.
 */
abstract class BaseMergeDataResource<ResultType, RequestType: MergeData<*,*>>(
    dataWrapperDao: DataWrapperDao,
    appExecutor: AppExecutor
): BaseNetworkBoundResource<ResultType, RequestType>(dataWrapperDao, appExecutor){

    private var stashNetworkData: Response<RequestType>? = null

    final override fun saveCallResult(item: RequestType) {
        if(!item.networkData1Empty){
            saveFirstData(item.data1)
        }
        if(!item.networkData2Empty){
            saveSecondData(item.data2)
        }
    }

    final override fun loadFromDb(): LiveData<ResultType> {
        val mutableLiveData = MutableLiveData<ResultType>()
        appExecutor.ioExecutor.execute{
            val clazz = getRequestClass()
            val mergeDataType = clazz.genericSuperclass as ParameterizedType
            // MergeData types
            val firstDataType = mergeDataType.actualTypeArguments[0] as Class<*>
            val secondDataType = mergeDataType.actualTypeArguments[1] as Class<*>

            val firstData = tryToGetValidFirstData() ?: loadFirstDataFromDb(firstDataType)
            val secondData = tryToGetValidSecondData() ?: loadSecondDataFromDb(secondDataType)
            val mergeData = clazz.getDeclaredConstructor(firstDataType, secondDataType)
                .newInstance(firstData, secondData)
            mutableLiveData.postValue(transform(mergeData))
            // 判断在MergeData是空数据的情况下，是否要刷新数据到数据库，因为可能只是其中一个数据为空导致的
            if(needSaveResult()){
                saveCallResult(mergeData)
            }
        }
        return mutableLiveData
    }

    private fun needSaveResult(): Boolean{
        return stashNetworkData?.isSuccessful == true &&
                stashNetworkData?.code() == 204 &&
                (stashNetworkData?.body()?.isDataFresh() == true)
    }

    private fun tryToGetValidFirstData() :Any?{
        if(stashNetworkData?.isSuccessful == true && stashNetworkData?.code() == 204){
            return if(stashNetworkData?.body()?.networkData1Empty == false){
                stashNetworkData!!.body()!!.data1
            }else{
                null
            }
        }
        return null
    }

    private fun tryToGetValidSecondData() :Any?{
        if(stashNetworkData?.isSuccessful == true && stashNetworkData?.code() == 204){
            return if(stashNetworkData?.body()?.networkData2Empty == false){
                stashNetworkData!!.body()!!.data2
            }else{
                null
            }
        }
        return null
    }

    override fun stashNetworkData(response: Response<RequestType>) {
        super.stashNetworkData(response)
        stashNetworkData = response
    }

    abstract fun loadFirstDataFromDb(clazz: Class<*>): Any?
    abstract fun loadSecondDataFromDb(clazz: Class<*>): Any?

    abstract fun saveFirstData(data: Any?)
    abstract fun saveSecondData(data: Any?)
}