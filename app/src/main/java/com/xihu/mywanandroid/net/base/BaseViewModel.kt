package com.xihu.huidefeng.net.base

import androidx.lifecycle.*
import com.xihu.mywanandroid.net.beans.ConfigBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import java.lang.Exception

open class BaseViewModel : ViewModel(), LifecycleObserver {

    private val error by lazy { MutableLiveData<Exception>() }
    private val loading by lazy { MutableLiveData<Boolean>() }
    val error_state = MutableLiveData(false) // 是否异常

    fun launchUI(block: suspend CoroutineScope.()->Unit) = viewModelScope.launch {
        loading.value = true
        error_state.value = false
        try {
            // 超时则抛出异常TimeoutCancellationException
            withTimeoutOrNull(ConfigBean.instance.Retrofit.requestTimeout) {
                block() // 此处切换到线程池的上下文.
            }
        } catch (e: Exception) {
            onError()
            println("exception: $e")
            error.value = e
            error_state.value = true
            println("now is here!")
        } finally {
            loading.value = false
        }
    }

    fun getError(): LiveData<Exception> {
        return error
    }

    fun loading(): LiveData<Boolean> {
        return loading
    }


    open fun onError() {}
}