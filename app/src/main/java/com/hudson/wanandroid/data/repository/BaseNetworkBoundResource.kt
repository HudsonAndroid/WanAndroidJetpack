package com.hudson.wanandroid.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hudson.wanandroid.data.db.DataWrapperDao
import com.hudson.wanandroid.data.entity.DataWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

/**
 * for json object data entity
 * Created by Hudson on 2020/7/14 0014.
 */
abstract class BaseNetworkBoundResource<ResultType, RequestType>(
    private val dataWrapperDao: DataWrapperDao
) :NetworkBoundResource<ResultType, RequestType>(){
    override fun saveCallResult(item: RequestType) {
        val dataWrapper = DataWrapper(getRequestClass() as Class<Any>)
        dataWrapper.customInfo = identityInfo()
        dataWrapper.content = Gson().toJson(item)
        dataWrapperDao.insert(dataWrapper)
    }

    private fun getRequestClass(): Class<RequestType>{
        val type = javaClass.genericSuperclass as ParameterizedType
        return type.actualTypeArguments[1] as Class<RequestType>
    }

    override fun loadFromDb(): LiveData<ResultType> {
        val mutableLiveData = MutableLiveData<ResultType>()
        GlobalScope.launch(Dispatchers.IO) {
            //数据库访问，异步处理
            val clazz = getRequestClass()
            val wrapper = dataWrapperDao.queryExactly(clazz.name, identityInfo())
            val source = wrapper?.content?.run {
                val jsonEntity = Gson().fromJson(this, clazz)
                transform(jsonEntity)
            }
            mutableLiveData.postValue(source)
        }
        return mutableLiveData
    }

    abstract fun transform(requestType: RequestType): ResultType

    //区分同一类数据的额外标识
    open fun identityInfo() = ""
}