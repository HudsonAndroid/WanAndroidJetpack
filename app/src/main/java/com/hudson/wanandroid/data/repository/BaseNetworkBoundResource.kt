package com.hudson.wanandroid.data.repository

import android.util.Log
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
) :NetworkBoundResource<ResultType, RequestType>(false){

    @Volatile
    private var isExpired = false

    init {
        load() //调用父类初始化操作，不能在父类直接使用，因为dataWrapperDao在父类中还没有初始化
    }

    override fun shouldFetch(data: ResultType?): Boolean {
        return isExpired
    }

    override fun saveCallResult(item: RequestType) {
        val clazz = getRequestClass()
        val dataWrapper = DataWrapper(clazz)
        dataWrapper.customInfo = identityInfo()
        dataWrapper.content = Gson().toJson(item)

        val age = DataExpireConfig.getAge(clazz)
        if(age != 0L){ // if we need manage data age
            dataWrapper.expireTime = System.currentTimeMillis() + age
        }
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
            checkDataExpire(wrapper)
            val source = wrapper?.content?.run {
                val jsonEntity = Gson().fromJson(this, clazz)
                transform(jsonEntity)
            }
            mutableLiveData.postValue(source)
        }
        return mutableLiveData
    }

    private fun checkDataExpire(dataWrapper: DataWrapper?){
        dataWrapper?.apply {
            if(content != null && expireTime != 0L && System.currentTimeMillis() >= expireTime){
                Log.d(javaClass.simpleName,"data is expired, type: "+ content?.javaClass)
                isExpired = true // expire tag
            }
        }
    }

    abstract fun transform(requestType: RequestType): ResultType

    //区分同一类数据的额外标识
    open fun identityInfo() = ""
}