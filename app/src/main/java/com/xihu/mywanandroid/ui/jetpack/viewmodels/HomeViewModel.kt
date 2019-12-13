package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.xihu.huidefeng.net.base.BaseViewModel
import com.xihu.huidefeng.net.repository.RemoteRepository
import com.xihu.mywanandroid.net.beans.Banner
import com.xihu.mywanandroid.net.beans.ListArticles
import com.xihu.mywanandroid.net.beans.Article
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

class HomeViewModel : BaseViewModel() {

    private val page = AtomicInteger(0)

    val topArticles = MutableLiveData<List<Article>>()
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

        onceLoading.value = false
        coroutineScope {
            async {
                val articles = repository.topArticles().data.also {
                    for (item in it) {
                        item.stick = true
                    }
                }

                println("Home_Top_Articles: $articles")
                topArticles.value = articles
            }

            async {
             topBanners.value = repository.topBanners().data
            }

            async {
             homeArticles.value = repository.homeArticles(page.getAndIncrement()).data
            }
        }
        onceLoading.value = true
    }
}