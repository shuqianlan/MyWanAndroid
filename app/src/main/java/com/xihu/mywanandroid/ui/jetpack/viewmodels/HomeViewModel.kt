package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.MutableLiveData
import com.xihu.huidefeng.net.base.BaseViewModel
import com.xihu.huidefeng.net.repository.RemoteRepository
import com.xihu.mywanandroid.net.beans.Article
import java.util.concurrent.atomic.AtomicInteger

class HomeViewModel : BaseViewModel() {
//    val bannerData = MutableLiveData<>
    val topArticles:MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>().also {
            loadTopArticles()
        }
    }

    val homeArticles:MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>().also {
            loadHomeArticles()
        }
    }

    private val repository = RemoteRepository.instance

    private fun loadTopArticles() = launchUI {

        println("loadTopArticles ............................. ")
        val response = repository.topArticles()
        println("HomeViewModel response $response")
        val datas = response.data
        println("loadTopArticles datas: $datas")
        topArticles.value = datas
    }

    private val page = AtomicInteger(0)
    public fun loadHomeArticles() = launchUI {
        println("loadTopArticles ............................. ")
        val response = repository.homeArticles(page.getAndIncrement())
        println("HomeViewModel response $response")
        val datas = response.data
        println("loadHomeArticles datas: $datas")
        topArticles.value = datas
    }

}