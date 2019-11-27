package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.MutableLiveData
import com.xihu.huidefeng.net.base.BaseViewModel
import com.xihu.huidefeng.net.repository.RemoteRepository
import com.xihu.mywanandroid.net.beans.Article

class HomeViewModel : BaseViewModel() {
//    val bannerData = MutableLiveData<>
    val topArticles:MutableLiveData<List<Article>> by lazy {
        MutableLiveData<List<Article>>().also {
            loadTopArticles()
        }
    }

    private fun loadTopArticles() = launchUI {

        print("loadTopArticles ............................. ")
        val response = RemoteRepository.instance.topArticles()
        print("HomeViewModel response $response")
        val datas = response.data
        print("datas: $datas")
        topArticles.value = datas
    }
}