package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.ui.jetpack.viewmodels.SystemViewModel

class SystemmFragment : BaseViewModelFragment<SystemViewModel>() {

    override fun providerViewModelClazz()=SystemViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_systemm, container, false)
    }


    override fun initViewModel() {

    }
}
