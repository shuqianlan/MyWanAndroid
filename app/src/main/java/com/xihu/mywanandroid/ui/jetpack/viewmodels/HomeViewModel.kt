package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.xihu.huidefeng.net.base.BaseViewModel
import com.xihu.huidefeng.net.repository.RemoteRepository
import com.xihu.mywanandroid.net.beans.Banner
import com.xihu.mywanandroid.net.beans.ListArticles
import com.xihu.mywanandroid.net.beans.TopArticle
import kotlinx.android.synthetic.main.home_article.view.*
import java.util.concurrent.atomic.AtomicInteger

class HomeViewModel : BaseViewModel() {
    val topArticles:MutableLiveData<List<TopArticle>> by lazy {
        MutableLiveData<List<TopArticle>>().also {
            loadTopArticles()
        }
    }
    val homeArticles:MutableLiveData<ListArticles> by lazy {
        MutableLiveData<ListArticles>().also {
            loadHomeArticles()
        }
    }
    val topBanners:MutableLiveData<List<Banner>> by lazy {
        MutableLiveData<List<Banner>>().also {
            loadTopBanners()
        }
    }

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

    private val page = AtomicInteger(0)
    fun loadHomeArticles() = launchUI {
        val response = repository.homeArticles(page.getAndIncrement())
        homeArticles.value = response.data
    }

}