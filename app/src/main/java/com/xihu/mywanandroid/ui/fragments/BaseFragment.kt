package com.xihu.mywanandroid.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.xihu.huidefeng.net.base.BaseViewModel

abstract class BaseFragment<VM:BaseViewModel> : Fragment() {
    protected lateinit var viewModel:VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        providerViewModelClazz().also {
            viewModel = ViewModelProviders.of(this).get(it)
            with(viewModel) {
                getError().observe(viewLifecycleOwner, Observer {
                    println("Exception $it")
                })

                loading().observe(viewLifecycleOwner, Observer {
                    print("Loading $it")
                })
            }
        }
    }

    abstract fun providerViewModelClazz(): Class<VM>

}