package com.hudson.wanandroid.network.common

import com.hudson.wanandroid.data.entity.wrapper.Status
import java.util.concurrent.CountDownLatch

/**
 * Created by Hudson on 2020/7/24.
 */
class TestHelp {
    val latch: CountDownLatch = CountDownLatch(1)

    fun notifyCompleted(status: Status){
        if(status == Status.SUCCESS || status == Status.ERROR){
            latch.countDown()
        }
    }
}