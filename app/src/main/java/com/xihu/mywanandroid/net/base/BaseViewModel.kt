package com.xihu.mywanandroid.net.base

import androidx.lifecycle.*
import com.xihu.mywanandroid.net.repository.RemoteRepository
import com.xihu.mywanandroid.net.beans.ConfigBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger

open class BaseViewModel : ViewModel(), LifecycleObserver {

    val exception by lazy { MutableLiveData<Exception>() }
    val error by lazy { MutableLiveData(false) }
    val loading by lazy { MutableLiveData(false) }
    val once by lazy { MutableLiveData(false) }
    private val atomic = AtomicInteger(0)

    val repository = RemoteRepository.instance

    fun launchUI(block: suspend CoroutineScope.()->Unit) = viewModelScope.launch {
        error.value = false
        loading.value = true
        once.value = atomic.getAndIncrement() == 0 // 仅首次会调用到此处!

        try {
            withTimeout(ConfigBean.instance.Retrofit.requestTimeout) {
                block() // 此处切换到线程池的上下文.
            }
        } catch (e: Exception) {
            exception.value = e
            error.value = true
        } finally {
            loading.value = false
            once.value = false
        }
    }

}