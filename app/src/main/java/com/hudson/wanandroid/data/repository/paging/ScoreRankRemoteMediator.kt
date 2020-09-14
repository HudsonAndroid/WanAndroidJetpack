package com.hudson.wanandroid.data.repository.paging

import com.hudson.wanandroid.data.WanAndroidApi
import com.hudson.wanandroid.data.db.WanAndroidDb
import com.hudson.wanandroid.data.entity.UserScore

/**
 * Created by Hudson on 2020/9/13.
 */
class ScoreRankRemoteMediator(
    api: WanAndroidApi,
    db: WanAndroidDb
): BaseRemoteMediator<UserScore>(api, db){
    private var nextKey: Int? = null

    override suspend fun fetchNetworkData(nextPageKey: Int?): List<UserScore> {
        return nextPageKey?.run {
            val userScoreBoard = api.userScoreBoard(nextPageKey)
            nextKey = userScoreBoard.data.curPage + 1
            return userScoreBoard.data.datas
        } ?: mutableListOf()
    }

    override fun getNextKey(nextPageKey: Int?) = nextKey

    override suspend fun updateNetworkData(db: WanAndroidDb, data: List<UserScore>) {
        for (datum in data) {
            datum.pagingTag = getPagingTag()
        }
        db.userScoreDao().insertUserScoreList(data)
    }

    override suspend fun cleanLocalData(db: WanAndroidDb) {
        db.userScoreDao().cleanUserScoreRank(getPagingTag())
    }

    fun getPagingTag() = PAGING_TAG

    companion object{
        const val PAGING_TAG = "ScoreRank"
    }
}