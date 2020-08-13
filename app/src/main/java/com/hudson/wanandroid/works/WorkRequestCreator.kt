package com.hudson.wanandroid.works

import android.os.Build
import androidx.work.Constraints
import androidx.work.PeriodicWorkRequest
import java.util.concurrent.TimeUnit

/**
 * Created by Hudson on 2020/8/12.
 */
fun createPeriodicRequest(): PeriodicWorkRequest{
    // 构建约束条件
    val builder = Constraints.Builder()
        .setRequiresBatteryNotLow(true)//非低电量
    if(Build.VERSION.SDK_INT >= 23){
        builder.setRequiresDeviceIdle(true) // 设备空闲
    }
    val constraints = builder.build()

    return PeriodicWorkRequest.Builder(SearchHistoryCleanWork::class.java,
        1, TimeUnit.DAYS) // 1填清理一次
        .addTag(SearchHistoryCleanWork.TAG)
        .setConstraints(constraints)
        .build()
}