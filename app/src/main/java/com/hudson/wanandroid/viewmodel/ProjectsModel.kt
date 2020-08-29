package com.hudson.wanandroid.viewmodel

import androidx.lifecycle.ViewModel
import com.hudson.wanandroid.data.repository.ProjectsRepository
import javax.inject.Inject

/**
 * Created by Hudson on 2020/8/14.
 */
class ProjectsModel @Inject constructor(private val repository: ProjectsRepository): ViewModel(){

    val projectCategory = repository.loadProjectsCategory()

    fun retry(){
        repository.retry()
    }

    fun refresh() = repository.refresh()
}