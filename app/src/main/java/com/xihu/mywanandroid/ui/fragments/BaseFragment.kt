package com.xihu.mywanandroid.ui.fragments

import android.os.Bundle
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xihu.huidefeng.net.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseFragment<VM:BaseViewModel> : Fragment(), CoroutineScope by MainScope() {
    protected lateinit var viewModel:VM
    val network_status = ObservableBoolean(false)

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
        }

        lifecycle.addObserver(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
        cancel()
    }

    abstract fun providerViewModelClazz(): Class<VM>


}