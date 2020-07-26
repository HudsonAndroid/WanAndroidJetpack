package com.hudson.wanandroid.data.repository.base

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.Call
import com.hudson.wanandroid.data.common.mergecall.RetrofitCall
import com.hudson.wanandroid.data.db.DataWrapperDao
import com.hudson.wanandroid.data.entity.DataWrapper
import com.hudson.wanandroid.data.repository.datawrapperexpire.DataExpireConfig
import java.lang.reflect.ParameterizedType

/**
 * for json object data entity
 * Created by Hudson on 2020/7/14.
 */
abstract class BaseNetworkBoundResource<ResultType, RequestType>(
    protected val dataWrapperDao: DataWrapperDao,
    appExecutor: AppExecutor
) : NetworkBoundResource<ResultType, RequestType>(false, appExecutor){

    @Volatile
    protected var isExpired = false

    init {
        load() //调用父类初始化操作，不能在父类直接使用，因为dataWrapperDao在父类中还没有初始化
    }

    /**
     * 你必须重新修改规则，否则将默认不会从网络获取数据，因为默认是使用isExpired（false）作为标识
     */
    override fun shouldFetch(data: ResultType?): Boolean {
        return isExpired
    }

    override fun saveCallResult(item: RequestType) {
        val clazz = getRequestClass()
        saveDataWrapper(
            clazz,
            item,
            dataWrapperDao,
            identityInfo()
        )
    }

    protected fun getRequestClass(): Class<RequestType>{
        val type = javaClass.genericSuperclass as ParameterizedType
        @Suppress("UNCHECKED_CAST")
        return type.actualTypeArguments[1] as Class<RequestType>
    }

    override fun loadFromDb(): LiveData<ResultType> {
        val mutableLiveData = MutableLiveData<ResultType>()
        appExecutor.ioExecutor.execute{
            //数据库访问，异步处理
            val clazz = getRequestClass()
            val wrapper =
                loadDataWrapper(clazz, dataWrapperDao)
            checkDataExpire(wrapper)
            val source = wrapper?.content?.run {
                val jsonEntity = Gson().fromJson(this, clazz)
                transform(jsonEntity)
            }
            mutableLiveData.postValue(source)
        }
        return mutableLiveData
    }

    protected fun checkDataExpire(dataWrapper: DataWrapper?){
        if(isDataWrapperExpire(dataWrapper)){
            Log.d(javaClass.simpleName,"data is expired, type: "+ dataWrapper?.content?.javaClass)
            isExpired = true // expire tag
        }
    }

    abstract fun transform(requestType: RequestType): ResultType?

    //区分同一类数据的额外标识
    open fun identityInfo() = ""
}




fun isDataWrapperExpire(dataWrapper: DataWrapper?): Boolean{
    return if(dataWrapper == null){
        false
    }else{
        return dataWrapper.run {
            content != null && expireTime != 0L && System.currentTimeMillis() >= expireTime
        }
    }
}

fun loadDataWrapper(clazz: Class<*>,dataWrapperDao: DataWrapperDao,
                        identityInfo: String = ""): DataWrapper? {
    return dataWrapperDao.queryExactly(clazz.name, identityInfo)
}

fun <T> convertTypeOrNull(input: Any?): T?{
    return if(input == null) null else input as T
}

fun <T> wrapperCall(call: retrofit2.Call<T>): Call<T> {
    return RetrofitCall(call)
}

fun <T> saveDataWrapper(clazz: Class<*>, content: T,
                    dataWrapperDao: DataWrapperDao,
                    identityInfo: String = ""){
    val dataWrapper = DataWrapper(clazz)
    dataWrapper.customInfo = identityInfo
    dataWrapper.content = Gson().toJson(content)

    val age = DataExpireConfig.getAge(clazz)
    if(age != 0L){ // if we need manage data age
        dataWrapper.expireTime = System.currentTimeMillis() + age
    }
    dataWrapperDao.insert(dataWrapper)
}