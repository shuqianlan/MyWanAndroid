package com.xihu.mywanandroid.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xihu.huidefeng.net.base.BaseViewModel
import kotlinx.coroutines.cancel

abstract class BaseViewModelFragment<VM: BaseViewModel> : BaseFragment() {
    protected lateinit var viewModel:VM

    abstract fun initViewModel()
    abstract fun providerViewModelClazz(): Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        providerViewModelClazz().also {
            viewModel = ViewModelProvider(this).get(it)
            viewModel.getError().observe(this, Observer {
                println("Exception $it")
            })

            viewModel.loading().observe(this, Observer {
                print("Loading $it")
            })

            initViewModel()
        }

        lifecycle.addObserver(viewModel)

    }

    open fun onRetryDatas(view: View) {}

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }
}