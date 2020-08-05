package com.hudson.wanandroid.flow

/**
 * Created by Hudson on 2020/8/5.
 */
sealed class ResultWrapper <out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()

    data class Error<out T>(val throwable: Throwable?): ResultWrapper<T>()
}