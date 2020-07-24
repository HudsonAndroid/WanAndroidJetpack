/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hudson.wanandroid.data.repository.base

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.hudson.wanandroid.data.common.AppExecutor
import com.hudson.wanandroid.data.common.mergecall.Call
import com.hudson.wanandroid.data.common.mergecall.Callback
import com.hudson.wanandroid.data.entity.BaseResult
import com.hudson.wanandroid.data.entity.wrapper.Resource
import retrofit2.Response

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
//参数表明是否直接在构造函数中初始化。主要原因是在构造函数中有开始加载数据，但部分变量可能还没初始化，例如Dao
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor(autoInit: Boolean = true, val appExecutor: AppExecutor) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        if(autoInit){
            load()
        }
    }

    protected fun load() {
        result.value = Resource.loading(null)
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { dbResult ->
            result.removeSource(dbSource)
            if (shouldFetch(dbResult)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) { // filter same data
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        createCall().enqueue(object : Callback<RequestType> {
            override fun onFailure(call: Call<RequestType>, t: Throwable?) {
                onFetchFailed()
                result.removeSource(dbSource)
                t?.printStackTrace()
                error(t?.message, dbSource)
            }

            override fun onResponse(call: Call<RequestType>, response: Response<RequestType>?) {
                result.removeSource(dbSource)
                if(response?.isSuccessful == true){
                    val data = response.body()
                    if(data == null || response.code() == 204){
                        // hook for MergeDataSource
                        stashNetworkData(response)
                        //found empty data, reload from disk
                        result.addSource(loadFromDb()){ newData->
                            setValue(Resource.success(newData))
                        }
                    }else{
                        //data entity judgement
                        if(data is BaseResult && !data.isSuccess()){
                            error(data.errorMsg, dbSource)
                            return //end
                        }
                        appExecutor.ioExecutor.execute{
                            saveCallResult(data)
                            appExecutor.mainThreadExecutor.execute{
                                //reload from disk
                                result.addSource(loadFromDb()){ newData ->
                                    setValue(Resource.success(newData))
                                }
                            }
                        }
                        // GlobalScope no use: avoid memory leak
//                        GlobalScope.launch(Dispatchers.Main) {
//                            withContext(Dispatchers.IO){
//                                saveCallResult(data)
//                            }
//                            //reload from disk
//                            result.addSource(loadFromDb()){ newData ->
//                                setValue(Resource.success(newData))
//                            }
//                        }
                    }
                }else{
                    val msg = response?.errorBody()?.string()
                    val errorMsg = if (msg.isNullOrEmpty()) {
                        response?.message()
                    } else {
                        msg
                    }
                    error(errorMsg, dbSource)
                }
            }
        })
    }

    private fun error(errorMsg:String?, dbSource: LiveData<ResultType>){
        result.addSource(dbSource){ newData->
            setValue(Resource.error(errorMsg?:"unknown error",newData))
        }
    }

    protected open fun stashNetworkData(response: Response<RequestType>){}

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): Call<RequestType>
}
