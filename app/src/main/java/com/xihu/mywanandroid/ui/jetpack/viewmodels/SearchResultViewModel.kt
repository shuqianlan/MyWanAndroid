package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.MutableLiveData
import com.xihu.huidefeng.net.base.BaseViewModel
import com.xihu.mywanandroid.net.beans.ListArticles
import java.util.concurrent.atomic.AtomicInteger

class SearchResultViewModel : BaseViewModel() {
    private val index = AtomicInteger(0)

    val searchResults = MutableLiveData<ListArticles>()
    val onceLoading = MutableLiveData<Boolean>(false)
    private var once = true

    fun search(key:String) = launchUI {

        if (once) {
            onceLoading.value = false
        }

        if (!key.isNullOrEmpty()) {
            searchResults.value = repository.searchKey(index.getAndIncrement(), key).data
        }

        if (once) {
            onceLoading.value = true
        }

        once = false
    }

    fun initialize(step:Int, key:String) {
        once = true
        index.set(step)
        search(key)
    }

}