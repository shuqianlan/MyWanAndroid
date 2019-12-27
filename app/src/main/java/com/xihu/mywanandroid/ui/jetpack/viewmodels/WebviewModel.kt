package com.xihu.mywanandroid.ui.jetpack.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xihu.huidefeng.net.base.BaseViewModel

class WebviewModel : BaseViewModel() {
    var isOnErr = MutableLiveData<Boolean>(false)

    fun isOnError(error:Boolean) {
        isOnErr.value = error
    }
}