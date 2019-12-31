package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.xihu.mywanandroid.net.base.BaseViewModel
import com.xihu.mywanandroid.net.beans.ListArticles
import com.xihu.mywanandroid.net.beans.SystemBean

class SystemViewModel :BaseViewModel() {
    val systems:MutableLiveData<List<SystemBean>> by lazy {
        MutableLiveData<List<SystemBean>>()
    }

    val systemArticles:MutableLiveData<ListArticles> by lazy {
        MutableLiveData<ListArticles>()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun loadSystems() = launchUI {
        repository.systems()?.also {
            systems.value = it.data
        }
    }

    fun loadSystemArticles(cid:Int) = launchUI {
        repository.systemArticles(0, cid)?.also {
            systemArticles.value = it.data
        }
    }
}