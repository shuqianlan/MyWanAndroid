package com.xihu.mywanandroid.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xihu.huidefeng.net.base.BaseViewModel
import com.xihu.mywanandroid.ui.activities.MainActivity
import com.xihu.mywanandroid.ui.interfaces.FragmentBackListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.Boolean

open class BaseFragment : Fragment(), CoroutineScope by MainScope(), FragmentBackListener {

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context as MainActivity).also {
            it.setBackForwardListener(this@BaseFragment)
        }
    }

    override fun onDetach() {
        super.onDetach()

        (context as MainActivity).also {
            it.setBackForwardListener(null)
        }
    }

    override fun onBackForawrd()=false
}