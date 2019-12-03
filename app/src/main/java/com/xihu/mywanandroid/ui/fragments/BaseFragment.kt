package com.xihu.mywanandroid.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.xihu.huidefeng.net.base.BaseViewModel

abstract class BaseFragment<VM:BaseViewModel> : Fragment() {
    protected lateinit var viewModel:VM

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
    }

    abstract fun providerViewModelClazz(): Class<VM>

}