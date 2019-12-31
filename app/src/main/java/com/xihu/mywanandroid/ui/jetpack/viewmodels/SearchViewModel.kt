package com.xihu.mywanandroid.ui.jetpack.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.system.Os
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.xihu.mywanandroid.net.base.BaseViewModel
import com.xihu.mywanandroid.net.repository.RemoteRepository
import com.xihu.mywanandroid.WanApp
import com.xihu.mywanandroid.net.beans.Hotkey
import com.xihu.mywanandroid.net.beans.SearchKey
import java.util.concurrent.atomic.AtomicInteger

class SearchViewModel : BaseViewModel() {

    private val SEARCH_KEYS_SP = "com.xihu.mywanandroid.ui.jetpack.viewmodels.SearchViewModel.SEARCH_KEYS"

    internal val hotkeys = MutableLiveData<List<Hotkey>>()
    internal val searchKeys = MutableLiveData<List<SearchKey>>()

    private val tempKeys = mutableListOf<String>("Kotlin", "Kotlin协程", "Android音视频开发", "Flutter", "注解", "Jetpack")

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun refreshHotkeys() = launchUI {
        hotkeys.value = RemoteRepository.instance.hotKeys().data

        searchKeys.value = (0..5).asSequence().map { index ->
            SearchKey(SEARCH_KEYS_SP+index, System.currentTimeMillis()-index*1000, tempKeys[index])
        }.toList()

    }

    fun addSearchKey(tag:String) {
        tempKeys.add(tag)
    }

    fun removeSearchKey(key:String) {
        tempKeys.remove(key)
    }

    fun clearSearchKeys() {
        tempKeys.clear()
    }
}