package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.xihu.huidefeng.net.base.BaseViewModel
import com.xihu.mywanandroid.net.beans.Article
import com.xihu.mywanandroid.net.beans.Project
import com.xihu.mywanandroid.net.beans.ProjectItems
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger

class ProjViewModel : BaseViewModel() {
    val projects = MutableLiveData<List<Project>>()
    val projectItems = MutableLiveData<ProjectItems>()

    val onceLoading = MutableLiveData<Boolean>()
    private var isLastest = false
    private var cid:Int = -1

    private val atomic = AtomicInteger(0)
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun loadProjects() = launchUI {
        onceLoading.value = false
        repository.loadProjects()?.also {
            projects.value = it.data

            delay(100)
            onceLoading.value = true
        }
    }

    fun resetLoadProjItems(cid:Int=0, isLatest:Boolean=false) {
        atomic.set(if (isLatest) 0 else 1)

        this.cid = cid
        isLastest = isLatest
        loadProjItems()
    }

    fun loadProjItems() = launchUI{
        val response = if (isLastest) {
            repository.lastestProjectItems(atomic.getAndIncrement())
        } else {
            repository.projectItems(atomic.getAndIncrement(), cid)
        }

        println("response: $response")
        response?.also {
            projectItems.value = it.data
        }

    }

}