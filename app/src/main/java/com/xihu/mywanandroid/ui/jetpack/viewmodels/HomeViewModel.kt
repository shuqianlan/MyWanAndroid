package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.xihu.huidefeng.net.base.BaseViewModel
import com.xihu.huidefeng.net.repository.RemoteRepository
import com.xihu.mywanandroid.net.beans.Article
import java.util.concurrent.atomic.AtomicInteger

class HomeViewModel : BaseViewModel() {
//    val bannerData = MutableLiveData<>
    val topArticles:MutableLiveData<List<Article>> = MutableLiveData<List<Article>>()
    val homeArticles:MutableLiveData<List<Article>> = MutableLiveData<List<Article>>()

    private val repository = RemoteRepository.instance

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun loadTopArticles() = launchUI {
        val response = repository.topArticles()
        val datas = response.data
        topArticles.value = datas
    }

    private val page = AtomicInteger(0)
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public fun loadHomeArticles() = launchUI {
        val response = repository.homeArticles(page.getAndIncrement())
        val datas = response.data
        println("loadHomeArticles datas: $datas")
        topArticles.value = datas
    }

}