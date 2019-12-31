package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.MutableLiveData
import com.xihu.mywanandroid.net.base.BaseViewModel
import com.xihu.mywanandroid.net.beans.ListArticles
import java.util.concurrent.atomic.AtomicInteger

class SearchResultViewModel : BaseViewModel() {
    private val index = AtomicInteger(0)

    val searchResults = MutableLiveData<ListArticles>()
    fun search(key:String) = launchUI {

        if (!key.isNullOrEmpty()) {
            searchResults.value = repository.searchKey(index.getAndIncrement(), key).data
        }

    }

    fun initialize(step:Int, key:String) {
        index.set(step)
        search(key)
    }

}