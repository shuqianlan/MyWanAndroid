package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.databinding.FragmentSystemmBinding
import com.xihu.mywanandroid.ui.jetpack.viewmodels.SystemViewModel
import com.xihu.mywanandroid.ui.view.TagSelectionView
import kotlinx.android.synthetic.main.content_fragment_system.*

class SystemmFragment : BaseViewModelFragment<SystemViewModel>() {

    override fun providerViewModelClazz()=SystemViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentSystemmBinding.inflate(layoutInflater, container, false).apply {
            viewModel = this@SystemmFragment.viewModel
            lifecycleOwner = this@SystemmFragment.viewLifecycleOwner
        }.root
    }

    override fun initViewModel() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tags_floor1.setTags(TagSelectionView.text_tags, TagSelectionView.text_tags)
        tags_floor2.setTags(TagSelectionView.text_tags, TagSelectionView.text_tags)
    }

    override fun onRetryDatas(view: View) {
        println("onRetryDatas .................. ")
    }

    fun onRetry(view: View) {
        onRetryDatas(view)
    }
}
