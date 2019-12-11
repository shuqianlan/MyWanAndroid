package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.xihu.huidefeng.net.base.BaseViewModel
import com.xihu.huidefeng.net.repository.RemoteRepository
import com.xihu.mywanandroid.net.beans.Banner
import com.xihu.mywanandroid.net.beans.ListArticles
import com.xihu.mywanandroid.net.beans.TopArticle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger

class HomeViewModel : BaseViewModel() {

    private val page = AtomicInteger(0)

    val topArticles = MutableLiveData<List<TopArticle>>()
    val homeArticles = MutableLiveData<ListArticles>()
    val topBanners = MutableLiveData<List<Banner>>()
    val onceLoading = MutableLiveData<Boolean>(false)

    private val repository = RemoteRepository.instance
    fun loadTopArticles() = launchUI {
        val response = repository.topArticles()
        topArticles.value = response.data
    }

    fun loadTopBanners() = launchUI {
        val response = repository.topBanners()
        print("TopBanners $response")
        topBanners.value = response.data
    }

    fun loadHomeArticles() = launchUI {
        val response = repository.homeArticles(page.getAndIncrement())
        homeArticles.value = response.data
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun loadHomeItems() = launchUI {
        val job1 = launch {
            val response = repository.topArticles()
            println("topArticles response $response")
            topArticles.value = response.data
        }
        val job2 = launch {
            val response = repository.topBanners()
            print("TopBanners response $response")
            topBanners.value = response.data
        }
        val job3 = launch {
            val response = repository.homeArticles(page.getAndIncrement())
            println("homeArticles response $response")
            homeArticles.value = response.data
        }

        job1.join()
        job2.join()
        job3.join()

        delay(100)
        onceLoading.value = true
        println("wtf....... ")
    }
}