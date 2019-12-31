package com.xihu.mywanandroid.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout

import com.xihu.mywanandroid.R
import com.xihu.mywanandroid.databinding.FragmentSystemmBinding
import com.xihu.mywanandroid.net.beans.Hotkey
import com.xihu.mywanandroid.net.beans.SystemBean
import com.xihu.mywanandroid.ui.jetpack.viewmodels.SystemViewModel
import com.xihu.mywanandroid.ui.view.TagSelectionView
import kotlinx.android.synthetic.main.content_fragment_system.*
import kotlinx.android.synthetic.main.fragment_search.*

class SystemmFragment : BaseViewModelFragment<SystemViewModel>() {

    override fun providerViewModelClazz()=SystemViewModel::class.java

    private val floor1Datas = mutableListOf<SystemBean>()
    private val floor2Datas = mutableMapOf<Int, List<SystemBean>>()

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
        viewModel.systems.observe(this, Observer {
            it.forEach { bean ->
                floor2Datas[bean.id] = bean.children
            }

            bindTabLayout(it)
        })

        viewModel.systemArticles.observe(this, Observer {
            println("loadArticles: $it")
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        systems_projects.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                tags_floor2.removeAllViews()
                val datas = floor2Datas[(p0!!.tag as SystemBean).id]
                datas?.also {
                    tags_floor2.setTags(it.map { bean->
                        bean.name
                    }, it)
                }
            }

        })
        tags_floor2.setTagClickListener(object :TagSelectionView.OnTagClickListener {
            override fun onTagClick(view: View) {
                val courseId = (view.tag as SystemBean).id
                viewModel.loadSystemArticles(courseId)
            }
        })
    }

    private fun bindTabLayout(datas:List<SystemBean>) {
        datas.forEach { bean ->
            systems_projects.newTab().apply {
                text = bean.name
                tag = bean
            }.also {
                systems_projects.addTab(it)
            }
        }


    }

    override fun onRetryDatas(view: View) {
        println("onRetryDatas .................. ")
    }

    fun onRetry(view: View) {
        onRetryDatas(view)
    }
}
