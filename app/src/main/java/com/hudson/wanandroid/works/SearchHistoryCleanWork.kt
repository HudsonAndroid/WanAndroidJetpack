package com.hudson.wanandroid.works

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.viewmodel.bindingadapter.MAX_SHOW_WORD_COUNT

/**
 * Created by Hudson on 2020/8/12.
 */
class SearchHistoryCleanWork(val context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams){
    companion object{
        const val TAG = "SearchHistoryCleanWorker"
    }

    override fun doWork(): Result {
        // query all history search strings
        // 由于LiveData没有observe目标的话，将无法得到值，因此需要room提供直接结果查询
        val db = WanAndroidDb
            .getInstance(context)
        val hotWordDao = db
            .hotWordDao()
        val value = hotWordDao.queryHistoryDirectly()

        if(value.size > MAX_SHOW_WORD_COUNT * 2){
            Log.d("SearchCleanWorker", "need cleaning search history data, current size ${value.size}")
            db.runInTransaction {
                // 清理部分多余数据
                // 1.先清空
                hotWordDao.cleanHistory()
                // 2.删除前部数据，留下最新的指定数据
                val subList = value.subList(0, MAX_SHOW_WORD_COUNT)
                // 3.重新插入（由于主键并不是id）
                hotWordDao.insertAll(subList)
            }
        }
        return Result.success()
    }

}