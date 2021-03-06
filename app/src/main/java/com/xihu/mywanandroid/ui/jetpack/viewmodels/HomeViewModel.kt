package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.xihu.mywanandroid.net.base.BaseViewModel
import com.xihu.mywanandroid.net.repository.RemoteRepository
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

    fun loadTopArticles() = launchUI {
        val response = repository.topArticles()
        topArticles.value = response.data
    }

    fun loadTopBanners() = launchUI {
        val response = repository.topBanners()
        topBanners.value = response.data
    }

    fun loadHomeArticles() = launchUI {
        val response = repository.homeArticles(page.getAndIncrement())
        homeArticles.value = response.data
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun loadHomeItems() = launchUI {
        coroutineScope {
            async {
                topArticles.value = repository.topArticles().data.asSequence().map {
                    it.stick = true
                    it
                }.toList()
            }

            async {
             topBanners.value = repository.topBanners().data
            }

            async {
             homeArticles.value = repository.homeArticles(page.getAndIncrement()).data
            }
        }
    }
}